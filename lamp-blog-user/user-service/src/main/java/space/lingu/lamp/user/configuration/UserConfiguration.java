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

package space.lingu.lamp.user.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.lingu.lamp.user.filter.UserInfoFilter;
import space.lingu.lamp.user.filter.UserInfoFilterChain;
import space.lingu.lamp.user.filter.UserInfoFormatValidateFilter;
import space.lingu.lamp.user.filter.UserInfoSensitiveWordFilter;

/**
 * @author RollW
 */
@Configuration
public class UserConfiguration {

    @Bean
    public UserInfoFilter userInfoFilter() {
        return UserInfoFilterChain.connect(
                new UserInfoFormatValidateFilter(),
                new UserInfoSensitiveWordFilter()
        );
    }
}
