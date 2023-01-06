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

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import space.lingu.lamp.ErrorCodeFinderChain;
import space.lingu.lamp.ErrorCodeMessageProvider;
import space.lingu.lamp.IoErrorCode;
import space.lingu.lamp.web.common.WebCommonErrorCode;
import space.lingu.lamp.web.service.auth.AuthErrorCode;
import space.lingu.lamp.web.service.user.UserErrorCode;

/**
 * @author RollW
 */
@Configuration
public class ErrorCodeConfiguration {

    @Bean
    public ErrorCodeMessageProvider errorCodeMessageProvider(MessageSource messageSource) {
        return new ErrorCodeMessageProviderImpl(messageSource);
    }

    @Bean
    public ErrorCodeFinderChain errorCodeFinderChain() {
        return ErrorCodeFinderChain.start(
                WebCommonErrorCode.getFinderInstance(),
                AuthErrorCode.getFinderInstance(),
                IoErrorCode.getFinderInstance(),
                UserErrorCode.getFinderInstance()
        );
    }

}
