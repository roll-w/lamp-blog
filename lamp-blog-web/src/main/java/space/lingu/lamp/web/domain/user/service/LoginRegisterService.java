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

package space.lingu.lamp.web.domain.user.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.Result;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.common.RequestInfo;
import space.lingu.lamp.web.domain.authentication.common.AuthErrorCode;
import space.lingu.lamp.web.domain.authentication.login.LoginStrategy;
import space.lingu.lamp.web.domain.authentication.login.LoginStrategyType;
import space.lingu.lamp.web.domain.authentication.login.LoginVerifiableToken;
import space.lingu.lamp.RequestMetadata;
import space.lingu.lamp.web.domain.user.RegisterVerificationToken;
import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.common.UserErrorCode;
import space.lingu.lamp.web.domain.user.dto.UserInfo;
import space.lingu.lamp.web.domain.user.dto.UserInfoSignature;
import space.lingu.lamp.web.domain.user.event.OnUserRegistrationEvent;
import space.lingu.lamp.web.domain.user.filter.UserInfoFilter;
import space.lingu.lamp.web.domain.user.repository.RegisterVerificationTokenRepository;
import space.lingu.lamp.web.domain.user.repository.UserRepository;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author RollW
 */
@Service
public class LoginRegisterService implements RegisterTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(LoginRegisterService.class);

    private final UserRepository userRepository;
    private final RegisterVerificationTokenRepository registerVerificationTokenRepository;
    private final UserManageService userManageService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Map<LoginStrategyType, LoginStrategy> loginStrategyMap =
            new EnumMap<>(LoginStrategyType.class);

    public LoginRegisterService(@NonNull List<LoginStrategy> strategies,
                                UserRepository userRepository,
                                RegisterVerificationTokenRepository registerVerificationTokenRepository,
                                UserInfoFilter userInfoFilter,
                                UserManageService userManageService,
                                ApplicationEventPublisher eventPublisher,
                                PasswordEncoder passwordEncoder,
                                AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.registerVerificationTokenRepository = registerVerificationTokenRepository;
        this.userManageService = userManageService;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        strategies.forEach(strategy ->
                loginStrategyMap.put(strategy.getStrategyType(), strategy));
    }

    public LoginStrategy getLoginStrategy(LoginStrategyType type) {
        return loginStrategyMap.get(type);
    }

    public void sendToken(long userId, LoginStrategyType type) throws IOException {
        LoginStrategy strategy = getLoginStrategy(type);
        User user = userRepository.getUserById(userId);
        LoginVerifiableToken token = strategy.createToken(user);
        RequestInfo requestInfo = new RequestInfo(LocaleContextHolder.getLocale(), null);
        strategy.sendToken(token, user, requestInfo);
    }

    public void sendToken(String identity, LoginStrategyType type) throws IOException {
        LoginStrategy strategy = getLoginStrategy(type);
        User user = tryGetUser(identity);
        LoginVerifiableToken token = strategy.createToken(user);
        RequestInfo requestInfo = new RequestInfo(LocaleContextHolder.getLocale(), null);
        strategy.sendToken(token, user, requestInfo);
    }

    private ErrorCode verifyToken(String token,
                                  User user,
                                  LoginStrategyType type) {
        LoginStrategy strategy = getLoginStrategy(type);
        return strategy.verify(token, user);
    }

    private User tryGetUser(String identity) {
        if (identity.contains("@")) {
            return userRepository.getUserByEmail(identity);
        }
        return userRepository.getUserByName(identity);
    }

    public Result<UserInfoSignature> loginUser(String identity,
                                               String token,
                                               RequestMetadata metadata,
                                               LoginStrategyType type) {
        Preconditions.checkNotNull(identity, "identity cannot be null");
        Preconditions.checkNotNull(token, "token cannot be null");

        User user = tryGetUser(identity);
        if (user == null) {
            return Result.of(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        ErrorCode code = verifyToken(token, user, type);
        if (code.failed()) {
            return Result.of(code);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return Result.success(
                UserInfoSignature.from(user)
        );
    }

    public Result<UserInfo> registerUser(String username, String password,
                                         String email) {
        boolean hasUsers = userRepository.hasUsers();
        Role role = hasUsers ? Role.USER : Role.ADMIN;
        boolean enabled = !hasUsers;
        Result<UserInfo> userInfoResult =
                userManageService.createUser(username, password, email, role, enabled);
        if (userInfoResult.failed()) {
            return userInfoResult;
        }

        if (!enabled) {
            OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                    userInfoResult.data(), Locale.getDefault(),
                    "http://localhost:5000/");
            // TODO: get url from config
            eventPublisher.publishEvent(event);
        }

        logger.info("Register username: {}, email: {}, role: {}, id: {}",
                username, email,
                userInfoResult.data().getRole(),
                userInfoResult.data().getUserId()
        );
        return userInfoResult;
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public String createRegisterToken(UserInfo userInfo) {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        long expiryTime = RegisterVerificationToken.calculateExpiryDate();
        RegisterVerificationToken registerVerificationToken = new RegisterVerificationToken(
                token, userInfo.id(), expiryTime, false
        );
        registerVerificationTokenRepository.insert(registerVerificationToken);
        return uuid.toString();

    }

    @Override
    public ErrorCode verifyRegisterToken(String token) {
        RegisterVerificationToken verificationToken =
                registerVerificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return AuthErrorCode.ERROR_TOKEN_NOT_EXIST;
        }
        if (verificationToken.used()) {
            return AuthErrorCode.ERROR_TOKEN_USED;
        }
        if (verificationToken.isExpired()) {
            return AuthErrorCode.ERROR_TOKEN_EXPIRED;
        }
        registerVerificationTokenRepository.makeTokenVerified(verificationToken);
        User user = userRepository
                .getUserById(verificationToken.userId());
        if (user.isCanceled()) {
            return UserErrorCode.ERROR_USER_CANCELED;
        }
        if (user.isEnabled()) {
            return UserErrorCode.ERROR_USER_ALREADY_ACTIVATED;
        }
        userRepository.makeUserEnabled(user);
        return CommonErrorCode.SUCCESS;
    }

    public ErrorCode resendToken(String username) {
        User user = userRepository.getUserByName(username);
        if (user == null) {
            return UserErrorCode.ERROR_USER_NOT_EXIST;
        }
        OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                UserInfo.from(user), ApiContextHolder.getContext().locale(),
                "http://localhost:5000/");
        eventPublisher.publishEvent(event);

        return CommonErrorCode.SUCCESS;
    }

}
