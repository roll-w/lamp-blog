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

package space.lingu.lamp.web.domain.user.event;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.authentication.event.OnUserRegistrationEvent;
import space.lingu.lamp.web.common.keys.MailConfigKeys;
import space.lingu.lamp.web.domain.push.*;
import space.lingu.lamp.web.domain.push.mail.MailPushUser;
import space.lingu.lamp.user.AttributedUser;
import space.lingu.lamp.authentication.register.RegisterTokenProvider;

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
        String token = registerTokenProvider.createRegisterToken(user).token();
        if (mailProperties == null || Strings.isNullOrEmpty(mailProperties.getHost())) {
            logger.debug("Not configure the mail, skip sending mail.");
            registerTokenProvider.verifyRegisterToken(token);
            return;
        }
        if (MailConfigKeys.isDisabled(mailProperties.getUsername())) {
            logger.debug("Mail is disabled, skip sending mail.");
            registerTokenProvider.verifyRegisterToken(token);
            return;
        }
        // TODO: read email template
        String confirmUrl = event.getUrl() + token;
        PushMessageBody messageBody = new HtmlMessageBuilder()
                .setTitle("[Lamp Blog] Registration Confirmation")
                .appendParagraph("Dear " + user.getUsername() + ",")
                .breakLine()
                .appendParagraph("You are now registering a new account, click the link below to confirm activate.")
                .breakLine().breakLine()
                .appendLink(confirmUrl, "Confirm Activate")
                .breakLine().breakLine()
                .appendParagraph("If you are not registering for this account, please ignore this message.")
                .breakLine()
                .appendParagraph("Sincerely, Lamp Blog Team.")
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
