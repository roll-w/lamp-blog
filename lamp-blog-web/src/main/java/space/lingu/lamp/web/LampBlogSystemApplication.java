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

package space.lingu.lamp.web;

import com.google.common.base.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import space.lingu.fiesta.Fiesta;
import space.lingu.lamp.setting.ConfigProvider;
import space.lingu.lamp.setting.InputStreamConfigReader;
import space.lingu.lamp.setting.ReadonlyConfigProvider;
import space.lingu.lamp.web.common.keys.ServerConfigKeys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "space.lingu.lamp")
@Fiesta
public class LampBlogSystemApplication {
    private static ConfigurableApplicationContext sContext;

    public LampBlogSystemApplication(ConfigurableApplicationContext context) {
        sContext = context;
    }

    public static void main(String[] args) {
        ConfigProvider localProvider = createLocalProvider(args);

        Map<String, Object> overrideProperties = new HashMap<>();
        overrideProperties.put("server.port", localProvider.get(ServerConfigKeys.PORT));
        overrideProperties.put("spring.application.name", "Lamp Blog");
        setupFixedProperties(overrideProperties);
        overrideProperties.put("lamp.config.local-provider", localProvider);

        SpringApplication application =
                new SpringApplication(LampBlogSystemApplication.class);
        application.setDefaultProperties(overrideProperties);
        application.setBanner(new LampBlogBanner());
        sContext = application.run(prepareArguments(args));
    }

    private static String[] prepareArguments(String[] args) {
        // temporarily disable arguments
        return new String[0];
    }

    private static void setupFixedProperties(Map<String, Object> properties) {
        properties.put("spring.web.resources.add-mappings", false);
        properties.put("spring.jackson.mapper.ACCEPT_CASE_INSENSITIVE_ENUMS", true);
        properties.put("web-common.context-initialize-filter", true);
        properties.put("spring.shell.history.enabled", false);
        properties.put("spring.config.location", "");
        properties.put("spring.messages.basename", "messages");

        // TODO: log level read from config
        properties.put("logging.level.space.lingu", "trace");
        properties.put("logging.level.web", "debug");
        properties.put("logging.level.org.springframework.security", "debug");
    }

    private static ConfigProvider createLocalProvider(String[] args) {
        String path = getConfigPath(args);
        boolean allowFail = Strings.isNullOrEmpty(path);

        try {
            return new ReadonlyConfigProvider(
                    InputStreamConfigReader.loadConfig(
                            LampBlogSystemApplication.class,
                            path, allowFail
                    )
            );
        } catch (IOException e) {
            throw new ServerInitializeException(e);
        }
    }

    public static final String CONFIG_PATH = "--config";
    public static final String SHORTAGE_CONFIG_PATH = "-c";

    private static String getConfigPath(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(CONFIG_PATH) || args[i].equals(SHORTAGE_CONFIG_PATH)) {
                if (i + 1 >= args.length) {
                    throw configPathNotSpecified(args[i]);
                }
                return args[i + 1];
            }
        }
        return null;
    }

    private static ServerInitializeException configPathNotSpecified(String argName) {
        return new ServerInitializeException("You are using " +
                "config file path option: '" + argName +
                "', but not specify the config file path.");
    }
}
