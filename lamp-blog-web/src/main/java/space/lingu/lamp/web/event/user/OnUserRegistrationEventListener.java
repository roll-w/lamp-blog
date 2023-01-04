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

package space.lingu.lamp.web.event.user;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.event.EventCallback;
import space.lingu.lamp.event.EventRegistry;
import space.lingu.lamp.web.common.MailConfigKeys;
import space.lingu.lamp.web.common.SimpleMailMessageBuilder;
import space.lingu.lamp.web.data.SettingLoader;
import space.lingu.lamp.web.data.dto.user.UserInfo;
import space.lingu.lamp.web.data.entity.SystemSetting;
import space.lingu.lamp.web.service.user.RegisterTokenProvider;

/**
 * @author RollW
 */
@Component
public class OnUserRegistrationEventListener implements ApplicationListener<OnUserRegistrationEvent>,
        EventCallback<SystemSetting> {
    private static final Logger logger = LoggerFactory.getLogger(OnUserRegistrationEventListener.class);

    private final RegisterTokenProvider registerTokenProvider;
    private final SettingLoader settingLoader;
    private final MailProperties mailProperties;
    private final JavaMailSender mailSender;

    private String username;

    public OnUserRegistrationEventListener(RegisterTokenProvider registerTokenProvider,
                                           SettingLoader settingLoader,
                                           MailProperties mailProperties,
                                           JavaMailSender mailSender,
                                           EventRegistry<SystemSetting, String> registry) {
        registry.register(this, MailConfigKeys.PREFIX);
        this.registerTokenProvider = registerTokenProvider;
        this.settingLoader = settingLoader;
        this.mailProperties = mailProperties;
        this.mailSender = mailSender;
        this.username = chooseUsername();
    }


    @Override
    public void onApplicationEvent(@NonNull OnUserRegistrationEvent event) {
        handleRegistration(event);
    }

    @Async
    void handleRegistration(OnUserRegistrationEvent event) {
        UserInfo userInfo = event.getUser();
        String token = registerTokenProvider.createRegisterToken(userInfo);
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
        // TODO: make configurable
        String subject = "[Lamp Blog] Registration Confirmation";
        String confirmUrl = event.getUrl() + token;
        SimpleMailMessage mailMessage = new SimpleMailMessageBuilder()
                .setTo(userInfo.email())
                .setSubject(subject)
                .setText(("Dear %s,\nYou are now registering a new account, " +
                        "click %s to confirm activate.\n" +
                        "If you are not registering for this account, please ignore this message.\n\n" +
                        "Sincerely, Lamp Blog Team.")
                        .formatted(userInfo.username(), confirmUrl))
                .setFrom(username)
                .build();
        mailSender.send(mailMessage);
    }


    private String chooseUsername() {
        String sender = settingLoader.getSettingValue(MailConfigKeys.KEY_MAIL_SENDER_NAME);
        if (sender == null) {
            return mailProperties.getUsername();
        }
        return sender + " <" + mailProperties.getUsername() + ">";
    }

    @Override
    public void onEvent(SystemSetting event) {
        // TODO: on event
    }
}
