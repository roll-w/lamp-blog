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
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.RequestMetadata;
import space.lingu.lamp.authentication.UserInfoSignature;
import space.lingu.lamp.authentication.event.OnUserLoginEvent;
import space.lingu.lamp.authentication.event.OnUserRegistrationEvent;
import space.lingu.lamp.authentication.login.LoginStrategy;
import space.lingu.lamp.authentication.login.LoginStrategyType;
import space.lingu.lamp.authentication.login.LoginVerifiableToken;
import space.lingu.lamp.authentication.register.RegisterTokenProvider;
import space.lingu.lamp.authentication.register.RegisterVerificationToken;
import space.lingu.lamp.authentication.register.repository.RegisterTokenDao;
import space.lingu.lamp.authentication.register.repository.RegisterTokenDo;
import space.lingu.lamp.user.repository.UserDao;
import space.lingu.lamp.user.repository.UserDo;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.user.AttributedUserDetails;
import space.lingu.lamp.user.AttributedUser;
import space.lingu.lamp.user.Role;
import space.lingu.lamp.user.UserIdentity;
import space.lingu.lamp.user.UserManageService;
import space.lingu.lamp.user.UserSignatureProvider;
import space.lingu.lamp.user.UserViewException;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.system.AuthenticationException;
import tech.rollw.common.web.system.ContextThreadAware;

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

    private final UserDao userRepository;
    private final RegisterTokenDao registerTokenRepository;
    private final UserManageService userManageService;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final UserSignatureProvider userSignatureProvider;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;
    private final Map<LoginStrategyType, LoginStrategy> loginStrategyMap =
            new EnumMap<>(LoginStrategyType.class);

    public LoginRegisterService(@NonNull List<LoginStrategy> strategies,
                                UserDao userRepository,
                                RegisterTokenDao registerTokenRepository,
                                UserManageService userManageService,
                                ApplicationEventPublisher eventPublisher,
                                AuthenticationManager authenticationManager,
                                UserSignatureProvider userSignatureProvider,
                                ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.userRepository = userRepository;
        this.registerTokenRepository = registerTokenRepository;
        this.userManageService = userManageService;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.userSignatureProvider = userSignatureProvider;
        this.apiContextThreadAware = apiContextThreadAware;
        strategies.forEach(strategy ->
                loginStrategyMap.put(strategy.getStrategyType(), strategy));
    }

    public LoginStrategy getLoginStrategy(LoginStrategyType type) {
        return loginStrategyMap.get(type);
    }

    public void sendToken(long userId, LoginStrategyType type) throws IOException {
        LoginStrategy strategy = getLoginStrategy(type);
        UserDo user = userRepository.getByUserId(userId);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        LoginVerifiableToken token = strategy.createToken(user);
        LoginStrategy.Options requestInfo = new LoginStrategy.Options(LocaleContextHolder.getLocale());
        strategy.sendToken(token, user, requestInfo);
    }

    public void sendToken(String identity, LoginStrategyType type) throws IOException {
        LoginStrategy strategy = getLoginStrategy(type);
        AttributedUserDetails user = tryGetUser(identity);
        LoginVerifiableToken token = strategy.createToken(user);
        LoginStrategy.Options requestInfo = new LoginStrategy.Options(LocaleContextHolder.getLocale());
        strategy.sendToken(token, user, requestInfo);
    }

    private ErrorCode verifyToken(String token,
                                  AttributedUserDetails user,
                                  LoginStrategyType type) {
        LoginStrategy strategy = getLoginStrategy(type);
        return strategy.verify(token, user);
    }

    private AttributedUserDetails tryGetUser(String identity) {
        if (identity.contains("@")) {
            return userRepository.getByEmail(identity);
        }
        return userRepository.getByUsername(identity);
    }

    public UserInfoSignature loginUser(String identity,
                                       String token,
                                       RequestMetadata metadata,
                                       LoginStrategyType type) {
        Preconditions.checkNotNull(identity, "identity cannot be null");
        Preconditions.checkNotNull(token, "token cannot be null");

        AttributedUserDetails user = tryGetUser(identity);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        ErrorCode code = verifyToken(token, user, type);
        if (code.failed()) {
            throw new UserViewException(code);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, token, user.getRole().toAuthority());
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        OnUserLoginEvent onUserLoginEvent = new OnUserLoginEvent(user, metadata);
        eventPublisher.publishEvent(onUserLoginEvent);
        String signature = userSignatureProvider.getSignature(user.getUserId());
        return UserInfoSignature.from(user, signature);
    }

    public AttributedUser registerUser(String username, String password,
                                       String email) {
        boolean hasUsers = userRepository.hasUsers();
        Role role = hasUsers ? Role.USER : Role.ADMIN;
        boolean enabled = !hasUsers;
        AttributedUser user =
                userManageService.createUser(username, password, email, role, enabled);

        if (!enabled) {
            OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                    user, Locale.getDefault(),
                    "http://localhost:5000/user/register/activate/");
            // TODO: get url from config
            eventPublisher.publishEvent(event);
        }

        logger.info("Register username: {}, email: {}, role: {}, id: {}",
                username, email,
                user.getRole(),
                user.getUserId()
        );
        return user;
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public String createRegisterToken(UserIdentity userIdentity) {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        long expiryTime = RegisterVerificationToken.calculateExpiryDate();
        RegisterTokenDo registerVerificationToken = new RegisterTokenDo(
                null, token, userIdentity.getUserId(), expiryTime, false
        );
        registerTokenRepository.save(registerVerificationToken);
        return uuid.toString();
    }

    @Override
    public void verifyRegisterToken(String token) {
        RegisterTokenDo verificationTokenDo =
                registerTokenRepository.findByToken(token);
        if (verificationTokenDo == null) {
            throw new AuthenticationException(AuthErrorCode.ERROR_TOKEN_NOT_EXIST);
        }
        // TODO: temporary compatible solution, need to be optimized
        RegisterVerificationToken verificationToken = verificationTokenDo.lock();
        if (verificationToken.used()) {
            throw new AuthenticationException(AuthErrorCode.ERROR_TOKEN_USED);
        }
        if (verificationToken.isExpired()) {
            throw new AuthenticationException(AuthErrorCode.ERROR_TOKEN_EXPIRED);
        }
        registerTokenRepository.makeTokenVerified(verificationToken.id());
        UserDo user = userRepository
                .getByUserId(verificationToken.userId());
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        if (user.isCanceled()) {
            throw new AuthenticationException(UserErrorCode.ERROR_USER_CANCELED);
        }
        if (user.isEnabled()) {
            throw new AuthenticationException(UserErrorCode.ERROR_USER_ALREADY_ACTIVATED);
        }
        // TODO: should not modify the original user object, at least should clone it
        user.setEnabled(true);
        user.setUpdateTime(System.currentTimeMillis());
        userRepository.save(user);
    }

    public void resendToken(String username) {
        AttributedUserDetails user = userRepository.getByUsername(username);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        ApiContext apiContext = apiContextThreadAware.getContextThread()
                .getContext();
        Locale locale = apiContext == null
                ? Locale.getDefault()
                : apiContext.getLocale();
        OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                user, locale,
                // TODO: get url from config
                "http://localhost:5000/user/register/activate/"
        );
        eventPublisher.publishEvent(event);
    }
}
