/*
 * Copyright (C) 2023 RollW
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.lamprism.lampray.security.authentication.registration.service;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import tech.lamprism.lampray.authentication.VerifiableToken;
import tech.lamprism.lampray.authentication.event.OnUserRegistrationEvent;
import tech.lamprism.lampray.push.HtmlMessageBuilder;
import tech.lamprism.lampray.push.PushMessageBody;
import tech.lamprism.lampray.push.PushMessageStrategy;
import tech.lamprism.lampray.push.PushMessageStrategyProvider;
import tech.lamprism.lampray.push.PushType;
import tech.lamprism.lampray.push.mail.MailConfigKeys;
import tech.lamprism.lampray.push.mail.MailPushUser;
import tech.lamprism.lampray.security.authentication.registration.RegisterTokenProvider;
import tech.lamprism.lampray.user.AttributedUser;

/**
 * @author RollW
 */
@Component
public class OnUserRegistrationEventListener implements ApplicationListener<OnUserRegistrationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationEventListener.class);

    private final RegisterTokenProvider registerTokenProvider;
    private final PushMessageStrategyProvider pushMessageStrategyProvider;
    private final MailProperties mailProperties;

    public OnUserRegistrationEventListener(RegisterTokenProvider registerTokenProvider,
                                           MailProperties mailProperties,
                                           PushMessageStrategyProvider pushMessageStrategyProvider) {
        this.pushMessageStrategyProvider = pushMessageStrategyProvider;
        this.registerTokenProvider = registerTokenProvider;
        this.mailProperties = mailProperties;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnUserRegistrationEvent event) {
        handleRegistration(event);
    }

    private void handleRegistration(@NonNull OnUserRegistrationEvent event) {
        AttributedUser user = event.getUser();
        if (user.isEnabled()) {
            logger.debug("User '{}' is already enabled, skip sending mail.", user.getUsername());
            return;
        }
        if (mailProperties == null || Strings.isNullOrEmpty(mailProperties.getHost())) {
            logger.debug("Not configure the mail, skip sending mail.");
            return;
        }
        if (MailConfigKeys.isDisabled(mailProperties.getUsername())) {
            logger.debug("Mail is disabled, skip sending mail.");
            return;
        }
        VerifiableToken registerToken = registerTokenProvider.createRegisterToken(user);
        // TODO
        String confirmUrl = "" + registerToken.token();
        // TODO: read email template
        PushMessageBody messageBody = new HtmlMessageBuilder()
                .setTitle("[Lampray] Registration Confirmation")
                .appendParagraph("Dear " + user.getUsername() + ",")
                .breakLine()
                .appendParagraph("You are now registering a new account, click the link below to confirm activate.")
                .breakLine().breakLine()
                .appendLink(confirmUrl, "Confirm Activate")
                .breakLine().breakLine()
                .appendParagraph("If you are not registering for this account, please ignore this message.")
                .breakLine()
                .appendParagraph("Sincerely, Lampray Team.")
                .build();
        PushMessageStrategy emailStrategy =
                pushMessageStrategyProvider.getPushMessageStrategy(PushType.EMAIL);
        emailStrategy.push(
                new MailPushUser(mailProperties.getUsername(), null),
                user,
                messageBody
        );
    }
}
