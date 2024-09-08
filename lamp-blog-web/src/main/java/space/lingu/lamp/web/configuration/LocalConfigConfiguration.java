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

import com.google.common.base.Strings;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import space.lingu.lamp.setting.ConfigProvider;
import space.lingu.lamp.setting.InputStreamConfigReader;
import space.lingu.lamp.setting.ReadonlyConfigProvider;
import space.lingu.lamp.web.LampBlogSystemApplication;

import java.io.IOException;

/**
 * @author RollW
 */
@Configuration
public class LocalConfigConfiguration {
    private final ConfigurableApplicationContext configurableApplicationContext;

    public LocalConfigConfiguration(ConfigurableApplicationContext configurableApplicationContext) {
        this.configurableApplicationContext = configurableApplicationContext;
    }

    @Bean
    @Order(0)
    public ConfigProvider localConfigProvider() throws IOException {
        String path = configurableApplicationContext.getEnvironment()
                .getProperty("lamp.config.path");
        if (Strings.isNullOrEmpty(path)) {
            path = "lamp.conf";
        }

        return new ReadonlyConfigProvider(
                InputStreamConfigReader.loadConfig(
                        LampBlogSystemApplication.class,
                        path
                )
        );
    }
}
