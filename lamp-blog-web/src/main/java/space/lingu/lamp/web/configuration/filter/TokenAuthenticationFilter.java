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
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import space.lingu.NonNull;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.domain.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.web.domain.authentication.token.TokenAuthResult;
import space.lingu.lamp.web.domain.user.UserDetailsService;
import space.lingu.lamp.web.domain.user.dto.UserInfo;
import space.lingu.lamp.web.util.RequestUtils;
import tech.rollw.common.web.BusinessRuntimeException;

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
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

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
        HttpMethod method = HttpMethod.resolve(request.getMethod());
        boolean isAdminApi = isAdminApi(requestUri);
        String remoteIp = RequestUtils.getRemoteIpAddress(request);

        try {
            Authentication existAuthentication =
                    SecurityContextHolder.getContext().getAuthentication();
            if (existAuthentication != null) {
                UserDetails userDetails = (UserDetails)
                        existAuthentication.getPrincipal();
                UserInfo userInfo = UserInfo.from(userDetails);
                setApiContext(isAdminApi, remoteIp, method, userInfo);
                filterChain.doFilter(request, response);
                return;
            }

            String token = loadToken(request);
            if (token == null || token.isEmpty()) {
                nullNextFilter(isAdminApi, remoteIp, method, request, response, filterChain);
                return;
            }

            Long userId = authenticationTokenService.getUserId(token);
            if (userId == null) {
                nullNextFilter(isAdminApi, remoteIp, method, request, response, filterChain);
                return;
            }
            UserDetails userDetails =
                    userDetailsService.loadUserByUserId(userId);
            if (userDetails == null) {
                nullNextFilter(isAdminApi, remoteIp, method, request, response, filterChain);
                return;
            }
            TokenAuthResult result = authenticationTokenService.verifyToken(token,
                    userDetails.getPassword());
            if (!result.success()) {
                // although there are anonymous api access that don't need provides token,
                // but as long as it provides token here, we have to verify it.
                // And throw exception when failed.
                throw new BusinessRuntimeException(result.errorCode());
            }

            UserInfo userInfo = UserInfo.from(userDetails);
            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            setApiContext(isAdminApi, remoteIp, method, userInfo);
            filterChain.doFilter(request, response);
        } finally {
            ApiContextHolder.clearContext();
        }
    }

    private static void setApiContext(boolean isAdminApi, String remoteIp,
                                      HttpMethod method, UserInfo userInfo) {
        ApiContextHolder.ApiContext apiContext = new ApiContextHolder.ApiContext(
                isAdminApi, remoteIp, LocaleContextHolder.getLocale(), method, userInfo);
        ApiContextHolder.setContext(apiContext);
    }

    private boolean isAdminApi(String requestUri) {
        if (requestUri == null || requestUri.length() <= 10) {
            return false;
        }

        return antPathMatcher.match("/api/{version}/admin/**", requestUri);
    }

    private void nullNextFilter(boolean isAdminApi, String remoteIp, HttpMethod method,
                                HttpServletRequest request, HttpServletResponse response,
                                FilterChain filterChain) throws IOException, ServletException {
        setApiContext(isAdminApi, remoteIp, method, null);
        filterChain.doFilter(request, response);
    }

    private String loadToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return request.getParameter("token");
        }
        return token;
    }
}
