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

package space.lingu.lamp.web.configuration;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.push.mail.MailConfigKeys;
import space.lingu.lamp.setting.ConfigReader;
import space.lingu.lamp.setting.SettingSpecification;
import space.lingu.lamp.setting.event.SettingValueChangedEvent;

import static space.lingu.lamp.web.configuration.MailConfiguration.applyProperties;
import static space.lingu.lamp.web.configuration.MailConfiguration.setProperties;

/**
 * @author RollW
 */
@Component
public class MailSettingListener implements ApplicationListener<SettingValueChangedEvent<?, ?>> {
    private final MailProperties properties;
    private final JavaMailSenderImpl sender;
    private final ConfigReader configReader;

    public MailSettingListener(MailProperties properties,
                               JavaMailSenderImpl sender,
                               ConfigReader configReader) {
        this.properties = properties;
        this.sender = sender;
        this.configReader = configReader;
    }

    private void onEvent() {
        setProperties(properties, configReader);
        applyProperties(properties, sender);
    }

    @Override
    public void onApplicationEvent(@NonNull SettingValueChangedEvent<?, ?> event) {
        SettingSpecification<?, ?> specification = event.getSpecification();
        if (specification.getKey().getName().startsWith(MailConfigKeys.PREFIX)) {
            onEvent();
        }
    }
}
