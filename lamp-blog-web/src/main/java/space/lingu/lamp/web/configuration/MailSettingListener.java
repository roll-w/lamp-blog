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
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;
import space.lingu.lamp.web.common.keys.MailConfigKeys;
import space.lingu.lamp.web.system.setting.SettingLoader;
import space.lingu.lamp.web.system.setting.SystemSetting;
import tech.rollw.common.event.EventCallback;
import tech.rollw.common.event.EventRegistry;

import static space.lingu.lamp.web.configuration.MailConfiguration.applyProperties;
import static space.lingu.lamp.web.configuration.MailConfiguration.setProperties;

/**
 * @author RollW
 */
@Component
public class MailSettingListener implements EventCallback<SystemSetting> {
    private final MailProperties properties;
    private final JavaMailSenderImpl sender;
    private final SettingLoader settingLoader;

    public MailSettingListener(MailProperties properties,
                               JavaMailSenderImpl sender,
                               SettingLoader settingLoader,
                               EventRegistry<SystemSetting, String> registry) {
        this.properties = properties;
        this.sender = sender;
        this.settingLoader = settingLoader;
        registry.register(this, MailConfigKeys.PREFIX);
    }

    @Override
    public void onEvent(SystemSetting event) {
        setProperties(properties, settingLoader);
        applyProperties(properties, sender);
    }
}
