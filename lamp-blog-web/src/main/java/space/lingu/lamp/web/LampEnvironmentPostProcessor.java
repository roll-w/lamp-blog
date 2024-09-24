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
import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import space.lingu.lamp.setting.ConfigProvider;
import space.lingu.lamp.setting.InputStreamConfigReader;
import space.lingu.lamp.setting.ReadonlyConfigProvider;
import space.lingu.lamp.web.common.keys.ServerConfigKeys;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author RollW
 */
public class LampEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String SETUP_PROPERTIES = "lampSetupProperties";

    private final Log logger;

    public LampEnvironmentPostProcessor(DeferredLogFactory deferredLogFactory) {
        this.logger = deferredLogFactory.getLog(LampEnvironmentPostProcessor.class);
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment,
                                       SpringApplication application) {
        String[] rawArgs = environment.getProperty(LampEnvKeys.RAW_ARGS, String[].class);
        if (rawArgs == null) {
            throw new IllegalStateException("Raw arguments not found.");
        }
        ConfigProvider localProvider = createLocalProvider(rawArgs);
        Map<String, Object> setupProperties = new HashMap<>();
        setupProperties.put("server.port", localProvider.get(ServerConfigKeys.PORT));
        setupProperties.put(LampEnvKeys.LOCAL_CONFIG_LOADER, localProvider);

        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(new MapPropertySource(
                SETUP_PROPERTIES, setupProperties
        ));
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
            throw new ServerInitializeException(new ServerInitializeException.Detail(
                    "Failed to load local config file, due to: " + e.getMessage(),
                    "Check the file path and file content."
            ), e);
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
        ServerInitializeException.Detail detail = new ServerInitializeException.Detail(
                "You are using config file path option: '" + argName +
                        "', but not specify the config file path.",
                "Use '" + argName + " <path>' to specify the config file path."
        );
        return new ServerInitializeException(detail);
    }
}
