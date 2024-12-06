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

package tech.lamprism.lampray.web.configuration;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import tech.lamprism.lampray.setting.ConfigProvider;
import tech.lamprism.lampray.web.LampEnvKeys;

/**
 * @author RollW
 */
@Configuration
public class LocalConfigConfiguration {
    public static final String LOCAL_CONFIG_PROVIDER = "localConfigProvider";

    private final ConfigurableApplicationContext configurableApplicationContext;

    public LocalConfigConfiguration(ConfigurableApplicationContext configurableApplicationContext) {
        this.configurableApplicationContext = configurableApplicationContext;
    }

    @Bean(LOCAL_CONFIG_PROVIDER)
    @Order(0)
    public ConfigProvider localConfigProvider() {
        ConfigProvider configProvider = configurableApplicationContext.getEnvironment()
                .getProperty(LampEnvKeys.LOCAL_CONFIG_LOADER, ConfigProvider.class);
        if (configProvider == null) {
            throw new IllegalStateException("No local config provider found");
        }
        return configProvider;
    }
}
