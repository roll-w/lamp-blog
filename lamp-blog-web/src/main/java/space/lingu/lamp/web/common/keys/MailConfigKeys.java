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
import space.lingu.lamp.setting.AttributedSettingSpecification;
import space.lingu.lamp.setting.SettingKey;
import space.lingu.lamp.setting.SettingSource;
import space.lingu.lamp.setting.SettingSpecificationBuilder;
import space.lingu.lamp.setting.SettingSpecificationSupplier;

import java.util.List;

/**
 * @author RollW
 */
public final class MailConfigKeys implements SettingSpecificationSupplier {
    public static final String EMAIL_DISABLED = "disable";

    public static final AttributedSettingSpecification<String, String> SMTP_SERVER_HOST =
            new SettingSpecificationBuilder<>(SettingKey.ofString("mail.smtp.server.host"))
                    .setDefaultValue("localhost")
                    .setRequired(false)
                    .setSupportedSources(SettingSource.VALUES)
                    .build();

    public static final AttributedSettingSpecification<Integer, Integer> SMTP_SERVER_PORT =
            new SettingSpecificationBuilder<>(SettingKey.ofInt("mail.smtp.server.port"))
                    .setDefaultValue(25)
                    .setRequired(false)
                    .setSupportedSources(SettingSource.VALUES)
                    .build();

    public static final AttributedSettingSpecification<Boolean, Boolean> SMTP_SSL_ENABLE =
            new SettingSpecificationBuilder<>(SettingKey.ofBoolean("mail.smtp.ssl.enable"))
                    .setDefaultValue(false)
                    .setRequired(false)
                    .setSupportedSources(SettingSource.VALUES)
                    .build();
    public static final AttributedSettingSpecification<String, String> MAIL_USERNAME =
            new SettingSpecificationBuilder<>(SettingKey.ofString("mail.username"))
                    .setDefaultValue(null)
                    .setRequired(false)
                    .setSupportedSources(SettingSource.VALUES)
                    .build();

    public static final AttributedSettingSpecification<String, String> MAIL_PASSWORD =
            new SettingSpecificationBuilder<>(SettingKey.ofString("mail.password"))
                    .setDefaultValue(null)
                    .setRequired(false)
                    .setSupportedSources(SettingSource.VALUES)
                    .build();

    public static final AttributedSettingSpecification<String, String> MAIL_SENDER_NAME =
            new SettingSpecificationBuilder<>(SettingKey.ofString("mail.nickname"))
                    .setDefaultValue("Lamp Blog")
                    .setRequired(false)
                    .setSupportedSources(SettingSource.VALUES)
                    .build();

    public static final String PREFIX = "mail.";

    private static final List<AttributedSettingSpecification<?, ?>> KEYS;

    static {
        KEYS = List.of(
                SMTP_SERVER_HOST,
                SMTP_SERVER_PORT,
                SMTP_SSL_ENABLE,
                MAIL_USERNAME,
                MAIL_PASSWORD,
                MAIL_SENDER_NAME
        );
    }

    private MailConfigKeys() {
    }

    public static boolean isDisabled(String username) {
        return EMAIL_DISABLED.equals(username);
    }

    @NonNull
    @Override
    public List<AttributedSettingSpecification<?, ?>> getSpecifications() {
        return KEYS;
    }

    public static final MailConfigKeys INSTANCE = new MailConfigKeys();
}
