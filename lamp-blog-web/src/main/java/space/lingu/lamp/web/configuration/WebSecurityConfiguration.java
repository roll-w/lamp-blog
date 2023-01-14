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

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import space.lingu.lamp.web.configuration.compenent.WebDelegateSecurityHandler;
import space.lingu.lamp.web.configuration.filter.CorsConfigFilter;
import space.lingu.lamp.web.configuration.filter.TokenAuthenticationFilter;
import space.lingu.lamp.web.domain.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.web.domain.user.service.UserDetailsService;

/**
 * @author RollW
 */
@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class WebSecurityConfiguration {
    private final UserDetailsService userDetailsService;

    public WebSecurityConfiguration(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security,
                                                   CorsConfigFilter corsConfigFilter,
                                                   TokenAuthenticationFilter tokenAuthenticationFilter,
                                                   AuthenticationEntryPoint authenticationEntryPoint,
                                                   AccessDeniedHandler accessDeniedHandler) throws Exception {


        security.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .antMatchers("/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().permitAll();
        security.userDetailsService(userDetailsService);

        security.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);

        security.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);
        security.addFilterBefore(tokenAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
        security.addFilterBefore(corsConfigFilter,
                TokenAuthenticationFilter.class);
        return security.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/css/**")
                .antMatchers("/404.html")
                .antMatchers("/500.html")
                .antMatchers("/error.html")
                .antMatchers("/html/**")
                .antMatchers("/js/**")
                .antMatchers("/api/user/login/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigFilter corsConfigFilter() {
        return new CorsConfigFilter();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(
            AuthenticationTokenService authenticationTokenService) {
        return new TokenAuthenticationFilter(
                authenticationTokenService,
                userDetailsService
        );
    }

    @Bean
    public WebDelegateSecurityHandler webDelegateSecurityHandler(
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return new WebDelegateSecurityHandler(resolver);
    }

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults(""); // removes the "ROLE_" prefix
    }
}
