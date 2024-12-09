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

package org.springframework.security.web.firewall;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * Delegate to a {@link HandlerExceptionResolver} when a {@link RequestRejectedException} is
 * thrown.
 *
 * @author RollW
 */
public class ExceptionResolveRequestRejectedHandler implements RequestRejectedHandler {

    private final HandlerExceptionResolver handlerExceptionResolver;

    public ExceptionResolveRequestRejectedHandler(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       RequestRejectedException requestRejectedException) {
        handlerExceptionResolver.resolveException(
                request, response, null, requestRejectedException
        );
    }
}
