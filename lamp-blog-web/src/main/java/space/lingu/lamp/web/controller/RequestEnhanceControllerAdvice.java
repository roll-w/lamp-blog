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

package space.lingu.lamp.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import space.lingu.lamp.RequestMetadata;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.util.RequestUtils;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@ControllerAdvice
public class RequestEnhanceControllerAdvice {
    private final ContextThreadAware<ApiContext> apiContextThreadAware;

    public RequestEnhanceControllerAdvice(
            ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @ModelAttribute
    public RequestMetadata requestMetadata(HttpServletRequest request) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        if (apiContextThread.hasContext()) {
            ApiContext apiContext = apiContextThread.getContext();
            return new RequestMetadata(
                    apiContext.getIp(),
                    request.getHeader("User-Agent")
            );
        }
        return new RequestMetadata(
                RequestUtils.getRemoteIpAddress(request),
                request.getHeader("User-Agent")
        );
    }
}
