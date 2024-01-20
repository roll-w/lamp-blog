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

package space.lingu.lamp.web.configuration.filter;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import tech.rollw.common.web.BusinessRuntimeException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @author RollW
 */
public class CorsConfigFilter implements Filter {
    private final HandlerExceptionResolver resolver;

    public CorsConfigFilter(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;

        String origin = request.getHeader("Origin");
        if (origin != null) {
            response.setHeader("Access-Control-Allow-Origin", origin);
        }

        String headers = request.getHeader("Access-Control-Request-Headers");
        if (headers != null) {
            response.setHeader("Access-Control-Allow-Headers", headers);
            response.setHeader("Access-Control-Expose-Headers", headers);
        }

        StringJoiner methods = new StringJoiner(", ");
        Arrays.stream(HttpMethod.values()).forEach(httpMethod ->
                methods.add(httpMethod.name()));

        response.setHeader("Access-Control-Allow-Methods", methods.toString());
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        try {
            chain.doFilter(request, response);
        } catch (BusinessRuntimeException e) {
            resolver.resolveException(request, response, null, e);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
