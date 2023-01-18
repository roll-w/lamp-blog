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

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import space.lingu.NonNull;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.domain.authentication.token.TokenAuthResult;
import space.lingu.lamp.web.domain.user.dto.UserInfo;
import space.lingu.lamp.web.domain.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.web.domain.user.service.UserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RollW
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationTokenService authenticationTokenService;
    private final UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(AuthenticationTokenService authenticationTokenService,
                                     UserDetailsService userDetailsService) {
        this.authenticationTokenService = authenticationTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        ApiContextHolder.clearContext();
        String requestUri = request.getRequestURI();
        boolean isAdminApi = isAdminApi(requestUri);
        String remoteIp = tryGetIpAddress(request);

        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                nullNextFilter(isAdminApi, remoteIp, request, response, filterChain);
                return;
            }

            String token = request.getHeader("Authorization");
            if (token == null || token.isEmpty()) {
                nullNextFilter(isAdminApi, remoteIp, request, response, filterChain);
                return;
            }

            Long userId = authenticationTokenService.getUserId(token);
            if (userId == null) {
                nullNextFilter(isAdminApi, remoteIp, request, response, filterChain);
                return;
            }
            UserDetails userDetails =
                    userDetailsService.loadUserByUserId(userId);
            if (userDetails == null) {
                nullNextFilter(isAdminApi, remoteIp, request, response, filterChain);
                return;
            }
            TokenAuthResult result = authenticationTokenService.verifyToken(token,
                    userDetails.getPassword());
            if (!result.state()) {
                nullNextFilter(isAdminApi, remoteIp, request, response, filterChain);
                return;
            }

            UserInfo userInfo = UserInfo.from(userDetails);
            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            setApiContext(isAdminApi, remoteIp, userInfo);
            filterChain.doFilter(request, response);
        } finally {
            ApiContextHolder.clearContext();
        }
    }

    private static void setApiContext(boolean isAdminApi, String remoteIp, UserInfo userInfo) {
        ApiContextHolder.ApiContext apiContext = new ApiContextHolder.ApiContext(
                isAdminApi, remoteIp, LocaleContextHolder.getLocale(), userInfo);
        ApiContextHolder.setContext(apiContext);
    }

    private boolean isAdminApi(String requestUri) {
        if (requestUri == null || requestUri.length() <= 10) {
            return false;
        }
        String adminStart = requestUri.substring(0, 10);
        return adminStart.equalsIgnoreCase("/api/admin");
    }

    private void nullNextFilter(boolean isAdminApi, String remoteIp,
                                HttpServletRequest request, HttpServletResponse response,
                                FilterChain filterChain) throws IOException, ServletException {
        setApiContext(isAdminApi, remoteIp, null);
        filterChain.doFilter(request, response);
    }

    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "X-Real-IP",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private static String tryGetIpAddress(HttpServletRequest request) {
        String ip = null;
        String ipAddresses = null;
        for (String header : HEADERS_TO_TRY) {
            ipAddresses = request.getHeader(header);
            if (!isInvalidIp(ipAddresses)) {
                break;
            }
        }
        if (!isInvalidIp(ipAddresses)) {
            ip = ipAddresses.split(",")[0];
        }
        if (isInvalidIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isInvalidIp(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
