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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import space.lingu.NonNull;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.web.domain.user.UserDetailsService;
import space.lingu.lamp.web.domain.user.UserInfo;
import space.lingu.lamp.web.domain.user.UserSignatureProvider;
import space.lingu.lamp.web.util.RequestUtils;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.system.AuthenticationException;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author RollW
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationTokenService authenticationTokenService;
    private final UserDetailsService userDetailsService;
    private final UserSignatureProvider userSignatureProvider;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public TokenAuthenticationFilter(AuthenticationTokenService authenticationTokenService,
                                     UserDetailsService userDetailsService,
                                     UserSignatureProvider userSignatureProvider,
                                     ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.authenticationTokenService = authenticationTokenService;
        this.userDetailsService = userDetailsService;
        this.userSignatureProvider = userSignatureProvider;
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        ContextThread<ApiContext> contextThread =
                apiContextThreadAware.assambleContextThread(null);

        String requestUri = request.getRequestURI();
        HttpMethod method = HttpMethod.valueOf(request.getMethod());
        boolean isAdminApi = isAdminApi(requestUri);
        String remoteIp = RequestUtils.getRemoteIpAddress(request);
        long timestamp = System.currentTimeMillis();

        ApiContext apiContext = new ApiContext(
                isAdminApi, remoteIp,
                LocaleContextHolder.getLocale(),
                method, null,
                timestamp, requestUri
        );

        try {
            Authentication existAuthentication =
                    SecurityContextHolder.getContext().getAuthentication();
            if (existAuthentication != null) {
                UserDetails userDetails = (UserDetails)
                        existAuthentication.getPrincipal();
                UserInfo userInfo = UserInfo.from(userDetails);
                setApiContext(apiContext, userInfo, contextThread);
                filterChain.doFilter(request, response);
                return;
            }

            String token = loadToken(request);
            if (token == null || token.isEmpty()) {
                setApiContext(apiContext, null, contextThread);
                filterChain.doFilter(request, response);
                return;
            }

            Long userId = authenticationTokenService.getUserId(token);
            if (userId == null) {
                setApiContext(apiContext, null, contextThread);
                filterChain.doFilter(request, response);
                return;
            }
            UserDetails userDetails = tryGetUserDetails(userId);
            String signature = userSignatureProvider.getSignature(userId);

            authenticationTokenService.verifyToken(token, signature);
            // Although there is anonymous api access that doesn't need provides token,
            // but as long as it provides token here, we have to verify it.

            UserInfo userInfo = UserInfo.from(userDetails);
            Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                    userDetails,
                    userDetails.getPassword(),
                    userDetails.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            setApiContext(apiContext, userInfo, contextThread);
            filterChain.doFilter(request, response);
        } finally {
            apiContextThreadAware.clearContextThread();
        }
    }

    @NonNull
    private UserDetails tryGetUserDetails(long id) {
        try {
            return userDetailsService.loadUserByUserId(id);
        } catch (UsernameNotFoundException e) {
            throw new AuthenticationException(
                    AuthErrorCode.ERROR_INVALID_TOKEN
            );
        }
    }

    private static void setApiContext(ApiContext rawContext, UserInfo userInfo,
                                      ContextThread<ApiContext> contextThread) {

        ApiContext apiContext = rawContext.fork(userInfo);
        contextThread.setContext(apiContext);
    }

    private boolean isAdminApi(String requestUri) {
        if (requestUri == null || requestUri.length() <= 10) {
            return false;
        }

        return antPathMatcher.match("/api/{version}/admin/**", requestUri) ||
                antPathMatcher.match("/api/admin/**", requestUri);
    }

    private String loadToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return request.getParameter("token");
        }
        return token;
    }
}
