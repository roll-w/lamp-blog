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

package space.lingu.lamp.web.common.keys;

import space.lingu.NonNull;
import space.lingu.lamp.setting.SettingKey;
import space.lingu.lamp.setting.SettingSpecification;
import space.lingu.lamp.setting.SimpleSettingSpec;

import java.util.List;

/**
 * @author RollW
 */
public final class MailConfigKeys {
    public static final String EMAIL_DISABLED = "disable";

    private static final String KEY_SMTP_SERVER_HOST = "mail.smtp.server.host";
    private static final String KEY_SMTP_SERVER_PORT = "mail.smtp.server.port";
    private static final String KEY_SMTP_SSL_ENABLE = "mail.smtp.ssl.enable";
    private static final String KEY_MAIL_USERNAME = "mail.username";
    private static final String KEY_MAIL_PASSWORD = "mail.password";
    private static final String KEY_MAIL_SENDER_NAME = "mail.nickname";

    public static final SettingSpecification<String, String> SMTP_SERVER_HOST =
            new SimpleSettingSpec<>(
                    SettingKey.ofString(KEY_SMTP_SERVER_HOST),
                    "localhost"
            );

    public static final SettingSpecification<Integer, Integer> SMTP_SERVER_PORT =
            new SimpleSettingSpec<>(
                    SettingKey.ofInt(KEY_SMTP_SERVER_PORT),
                    25
            );

    public static final SettingSpecification<Boolean, Boolean> SMTP_SSL_ENABLE =
            SimpleSettingSpec.ofBoolean(KEY_SMTP_SSL_ENABLE, false);

    public static final SettingSpecification<String, String> MAIL_USERNAME =
            new SimpleSettingSpec<>(
                    SettingKey.ofString(KEY_MAIL_USERNAME),
                    null
            );

    public static final SettingSpecification<String, String> MAIL_PASSWORD =
            new SimpleSettingSpec<>(
                    SettingKey.ofString(KEY_MAIL_PASSWORD),
                    null
            );

    public static final SettingSpecification<String, String> MAIL_SENDER_NAME =
            new SimpleSettingSpec<>(
                    SettingKey.ofString(KEY_MAIL_SENDER_NAME),
                    "Lamp Blog"
            );

    public static final String PREFIX = "mail.";

    public static final String[] KEYS = {
            KEY_SMTP_SERVER_HOST,
            KEY_SMTP_SERVER_PORT,
            KEY_SMTP_SSL_ENABLE,
            KEY_MAIL_USERNAME,
            KEY_MAIL_PASSWORD,
            KEY_MAIL_SENDER_NAME
    };

    public static boolean isDisabled(String username) {
        return EMAIL_DISABLED.equals(username);
    }

    private MailConfigKeys() {
    }
}
