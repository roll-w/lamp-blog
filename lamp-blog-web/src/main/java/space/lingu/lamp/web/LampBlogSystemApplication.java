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

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.OverrideSystemPropertiesEnvironment;
import space.lingu.fiesta.Fiesta;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "space.lingu.lamp", exclude = {
        FreeMarkerAutoConfiguration.class,
        ErrorMvcAutoConfiguration.class
})
@Fiesta
public class LampBlogSystemApplication {
    private static ConfigurableApplicationContext sContext;

    public LampBlogSystemApplication(ConfigurableApplicationContext context) {
        sContext = context;
    }

    public static void main(String[] args) {
        Map<String, Object> overrideProperties = new HashMap<>();
        overrideProperties.put("spring.application.name", "Lamp Blog");
        overrideProperties.put(LampEnvKeys.RAW_ARGS, args);
        setupFixedProperties(overrideProperties);
        ConfigurableEnvironment environment = new OverrideSystemPropertiesEnvironment(
                false,
                false
        );
        SpringApplicationBuilder builder = new SpringApplicationBuilder(LampBlogSystemApplication.class)
                .environment(environment)
                .bannerMode(Banner.Mode.LOG)
                .properties(overrideProperties)
                .listeners(new LoggingPostApplicationPreparedEventListener())
                .banner(new LampBlogBanner());
        SpringApplication application = builder.build();
        sContext = application.run(prepareArguments(args));
    }

    private static String[] prepareArguments(String[] args) {
        // temporarily disable arguments
        return new String[0];
    }

    private static void setupFixedProperties(Map<String, Object> properties) {
        properties.put("spring.web.resources.add-mappings", false);
        properties.put("spring.output.ansi.enabled", "always");
        properties.put("spring.jackson.mapper.ACCEPT_CASE_INSENSITIVE_ENUMS", true);
        properties.put("web-common.context-initialize-filter", true);
        properties.put("spring.shell.history.enabled", false);
        properties.put("spring.config.location", "");
        properties.put("spring.messages.basename", "messages");
        properties.put("spring.jmx.enabled", false);
        properties.put("spring.jpa.properties.hibernates.globally_quoted_identifiers", true);
    }
}
