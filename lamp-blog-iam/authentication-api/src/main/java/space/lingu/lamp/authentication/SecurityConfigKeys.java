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

package space.lingu.lamp.authentication;

import space.lingu.lamp.setting.AttributedSettingSpecification;
import space.lingu.lamp.setting.SettingKey;
import space.lingu.lamp.setting.SettingSource;
import space.lingu.lamp.setting.SettingSpecificationBuilder;

/**
 * @author RollW
 */
public class SecurityConfigKeys {
    public static final AttributedSettingSpecification<String, String> TOKEN_ISSUER =
            new SettingSpecificationBuilder<>(SettingKey.ofString("security.token.issuer"))
                    .setDefaultValue("Lamp Blog")
                    .setSupportedSources(SettingSource.VALUES)
                    .setTextDescription("Token issuer.")
                    .setRequired(true)
                    .build();


    public static final AttributedSettingSpecification<Long, Long> TOKEN_EXPIRE_TIME =
            new SettingSpecificationBuilder<>(SettingKey.ofLong("security.token.expire-time"))
                    .setDefaultValue(3600L)
                    .setTextDescription("Token expiration time in seconds.")
                    .setSupportedSources(SettingSource.VALUES)
                    .setRequired(true)
                    .build();

    public static final String PREFIX = "security.";

    private SecurityConfigKeys() {
    }
}
