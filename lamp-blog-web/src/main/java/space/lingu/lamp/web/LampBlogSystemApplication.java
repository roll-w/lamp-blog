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

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import space.lingu.fiesta.Fiesta;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication(scanBasePackages = "space.lingu.lamp")
@Fiesta
public class LampBlogSystemApplication {
    private static ConfigurableApplicationContext sContext;

    public static void main(String[] args) {
        Map<String, Object> overrideProperties = new HashMap<>();
        overrideProperties.put("spring.application.name", "Lamp Blog");
        overrideProperties.put("spring.web.resources.add-mappings", false);
        overrideProperties.put("spring.jackson.mapper.ACCEPT_CASE_INSENSITIVE_ENUMS", true);

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
}
