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

package space.lingu.lamp.authentication.register.service;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.LampException;
import space.lingu.lamp.RequestMetadata;
import space.lingu.lamp.authentication.UserInfoSignature;
import space.lingu.lamp.authentication.VerifiableToken;
import space.lingu.lamp.authentication.event.OnUserLoginEvent;
import space.lingu.lamp.authentication.event.OnUserRegistrationEvent;
import space.lingu.lamp.authentication.login.LoginProvider;
import space.lingu.lamp.authentication.login.LoginStrategy;
import space.lingu.lamp.authentication.login.LoginStrategyType;
import space.lingu.lamp.authentication.login.LoginVerifiableToken;
import space.lingu.lamp.authentication.register.RegisterProvider;
import space.lingu.lamp.authentication.register.RegisterTokenProvider;
import space.lingu.lamp.authentication.register.RegisterVerificationToken;
import space.lingu.lamp.authentication.register.repository.RegisterTokenDo;
import space.lingu.lamp.authentication.register.repository.RegisterTokenRepository;
import space.lingu.lamp.user.AttributedUser;
import space.lingu.lamp.user.AttributedUserDetails;
import space.lingu.lamp.user.Role;
import space.lingu.lamp.user.UserIdentity;
import space.lingu.lamp.user.UserManageService;
import space.lingu.lamp.user.UserSignatureProvider;
import space.lingu.lamp.user.UserViewException;
import space.lingu.lamp.user.repository.UserDo;
import space.lingu.lamp.user.repository.UserRepository;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.IoErrorCode;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.system.AuthenticationException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author RollW
 */
@Service
public class LoginRegisterService implements LoginProvider, RegisterTokenProvider, RegisterProvider {
    private static final Logger logger = LoggerFactory.getLogger(LoginRegisterService.class);

    private final UserRepository userRepository;
    private final RegisterTokenRepository registerTokenRepository;
    private final UserManageService userManageService;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationManager authenticationManager;
    private final UserSignatureProvider userSignatureProvider;
    private final Map<LoginStrategyType, LoginStrategy> loginStrategyMap =
            new EnumMap<>(LoginStrategyType.class);

    public LoginRegisterService(List<LoginStrategy> strategies,
                                UserRepository userRepository,
                                RegisterTokenRepository registerTokenRepository,
                                UserManageService userManageService,
                                ApplicationEventPublisher eventPublisher,
                                AuthenticationManager authenticationManager,
                                UserSignatureProvider userSignatureProvider) {
        this.userRepository = userRepository;
        this.registerTokenRepository = registerTokenRepository;
        this.userManageService = userManageService;
        this.eventPublisher = eventPublisher;
        this.authenticationManager = authenticationManager;
        this.userSignatureProvider = userSignatureProvider;
        strategies.forEach(strategy ->
                loginStrategyMap.put(strategy.getStrategyType(), strategy));
    }

    @NonNull
    public LoginStrategy getLoginStrategy(@NonNull LoginStrategyType type) {
        return loginStrategyMap.get(type);
    }

    public void sendToken(long userId,
                          LoginStrategyType type,
                          RequestMetadata requestMetadata) throws IOException {
        LoginStrategy strategy = getLoginStrategy(type);
        UserDo user = userRepository.getByUserId(userId).orElse(null);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        LoginVerifiableToken token = strategy.createToken(user);
        LoginStrategy.Options requestInfo = new LoginStrategy.Options(LocaleContextHolder.getLocale());
        sendToken(strategy, token, user, requestInfo);
    }

    @Override
    public void sendToken(@NonNull String identity,
                          @NonNull LoginStrategyType type,
                          RequestMetadata requestMetadata) {
        LoginStrategy strategy = getLoginStrategy(type);
        AttributedUserDetails user = tryGetUser(identity);
        LoginVerifiableToken token = strategy.createToken(user);
        LoginStrategy.Options requestInfo = new LoginStrategy.Options(LocaleContextHolder.getLocale());
        sendToken(strategy, token, user, requestInfo);
    }

    private void sendToken(LoginStrategy strategy,
                           LoginVerifiableToken token,
                           AttributedUserDetails user,
                           LoginStrategy.Options requestInfo) {
        try {
            strategy.sendToken(token, user, requestInfo);
        } catch (IOException e) {
            logger.error("Failed to send token to user: {}@{}, due to: {}",
                    user.getUserId(), user.getUsername(), e.getMessage(), e);
            throw new LampException(IoErrorCode.ERROR_IO);
        }
    }


    private ErrorCode verifyToken(String token,
                                  AttributedUserDetails user,
                                  LoginStrategyType type) {
        LoginStrategy strategy = getLoginStrategy(type);
        return strategy.verify(token, user);
    }

    private AttributedUserDetails tryGetUser(String identity) {
        if (identity.contains("@")) {
            return userRepository.getByEmail(identity).orElse(null);
        }
        return userRepository.getByUsername(identity).orElse(null);
    }

    @Override
    @NonNull
    public UserInfoSignature login(@NonNull String identity,
                                   @NonNull String token,
                                   @NonNull LoginStrategyType type,
                                   RequestMetadata metadata) {
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

        OnUserLoginEvent onUserLoginEvent = new OnUserLoginEvent(user, metadata);
        eventPublisher.publishEvent(onUserLoginEvent);
        String signature = userSignatureProvider.getSignature(user.getUserId());
        return UserInfoSignature.from(user, signature);
    }

    @NonNull
    @Override
    public AttributedUser register(@NonNull String username,
                                   @NonNull String password,
                                   @NonNull String email) {
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

    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public VerifiableToken createRegisterToken(UserIdentity userIdentity) {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        long expiryTime = RegisterVerificationToken.calculateExpiryDate();
        RegisterTokenDo registerVerificationToken = new RegisterTokenDo(
                null, token, userIdentity.getUserId(), expiryTime, false
        );
        registerVerificationToken = registerTokenRepository.save(registerVerificationToken);
        return registerVerificationToken.lock();
    }

    @Override
    public void verifyRegisterToken(String token) {
        RegisterTokenDo registerTokenDo =
                registerTokenRepository.findByToken(token);
        if (registerTokenDo == null) {
            throw new AuthenticationException(AuthErrorCode.ERROR_TOKEN_NOT_EXIST);
        }
        if (registerTokenDo.getUsed()) {
            throw new AuthenticationException(AuthErrorCode.ERROR_TOKEN_USED);
        }
        if (registerTokenDo.isExpired()) {
            throw new AuthenticationException(AuthErrorCode.ERROR_TOKEN_EXPIRED);
        }
        registerTokenDo.markVerified();
        registerTokenRepository.save(registerTokenDo);
        UserDo user = userRepository.getByUserId(registerTokenDo.getUserId())
                .orElse(null);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        if (user.isCanceled()) {
            throw new AuthenticationException(UserErrorCode.ERROR_USER_CANCELED);
        }
        if (user.isEnabled()) {
            throw new AuthenticationException(UserErrorCode.ERROR_USER_ALREADY_ACTIVATED);
        }
        user.setEnabled(true);
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    public void resendToken(String username) {
        AttributedUserDetails user = userRepository.getByUsername(username).orElse(null);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        Locale locale = LocaleContextHolder.getLocale();
        OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                user, locale,
                // TODO: get url from config
                "http://localhost:5000/user/register/activate/"
        );
        eventPublisher.publishEvent(event);
    }
}
