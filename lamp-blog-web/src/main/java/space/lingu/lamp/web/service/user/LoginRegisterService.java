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

package space.lingu.lamp.web.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.MessagePackage;
import space.lingu.lamp.web.authentication.login.LoginStrategy;
import space.lingu.lamp.web.authentication.login.LoginStrategyType;
import space.lingu.lamp.web.data.database.repository.UserRepository;
import space.lingu.lamp.web.data.dto.user.UserInfo;
import space.lingu.lamp.web.data.entity.LoginVerifiableToken;
import space.lingu.lamp.web.data.entity.user.RegisterVerificationToken;
import space.lingu.lamp.web.data.entity.user.Role;
import space.lingu.lamp.web.data.entity.user.User;
import space.lingu.lamp.web.event.user.OnUserRegistrationEvent;

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
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Map<LoginStrategyType, LoginStrategy> loginStrategyMap =
            new EnumMap<>(LoginStrategyType.class);

    public LoginRegisterService(@NonNull List<LoginStrategy> strategies,
                                UserRepository userRepository,
                                ApplicationEventPublisher eventPublisher,
                                PasswordEncoder passwordEncoder,
                                AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        strategies.forEach(strategy ->
                loginStrategyMap.put(strategy.getStrategyType(), strategy));
    }

    public LoginStrategy getLoginStrategy(LoginStrategyType type) {
        return loginStrategyMap.get(type);
    }

    public void sendToken(long userId, LoginStrategyType type) {
        LoginStrategy strategy = getLoginStrategy(type);
        User user = userRepository.getUserById(userId);
        LoginVerifiableToken token = strategy.createToken(user);
        strategy.sendToken(token);
    }

    private ErrorCode verifyToken(String token,
                                  User user,
                                  LoginStrategyType type) {
        LoginStrategy strategy = getLoginStrategy(type);
        return strategy.verify(token, user);
    }

    private ErrorCode verifyToken(LoginStrategyType type,
                                  LoginVerifiableToken token) {
        LoginStrategy strategy = getLoginStrategy(type);
        return strategy.verify(token);
    }

    private User tryGetUser(String identity) {
        if (identity.contains("@")) {
            return userRepository.getUserByEmail(identity);
        }
        return userRepository.getUserByName(identity);
    }

    public MessagePackage<UserInfo> loginUser(String identity,
                                              String token,
                                              LoginStrategyType type) {
        User user = tryGetUser(identity);
        if (user == null) {
            return MessagePackage.create(UserErrorCode.ERROR_USER_NOT_EXIST, null);
        }
        ErrorCode code = verifyToken(token, user, type);
        if (!code.getState()) {
            return MessagePackage.create(code, null);
        }
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(user, token, user.getAuthorities());
        authentication = authenticationManager.authenticate(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return MessagePackage.success(
                UserInfo.from(user)
        );
    }

    public MessagePackage<UserInfo> registerUser(String username, String password,
                                                 String email, Role role) {
        Long userId = userRepository.getUserIdByName(username);
        if (!User.isInvalidId(userId)) {
            return MessagePackage.create(UserErrorCode.ERROR_USER_EXISTED,
                    "A user with same name is existed.", null
            );
        }
        if (userRepository.getUserByEmail(email) != null) {
            return MessagePackage.create(UserErrorCode.ERROR_EMAIL_EXISTED,
                    "A user with same email is existed.", null
            );
        }
        long registerTime = System.currentTimeMillis();
        String encodedPassword = passwordEncoder.encode(password);
        // TODO: check params
        User.Builder builder = User.builder()
                .setUsername(username)
                .setEmail(email)
                .setRole(role)
                .setPassword(encodedPassword)
                .setRegisterTime(registerTime)
                .setLocked(false)
                .setEnabled(false)
                .setAccountExpired(false);
        if (!userRepository.hasUsers()) {
            builder.setEnabled(true)
                    .setRole(Role.ADMIN);
        }
        User user = builder.build();
        if (!user.isEnabled()) {
            OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                    UserInfo.from(user), Locale.getDefault(), "");
            eventPublisher.publishEvent(event);
        }
        long id = userRepository.insertUser(user);
        logger.info("Register username: {}, email: {}, role: {}, id: {}",
                username, email, role, id);
        return MessagePackage.success(UserInfo.from(user));
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    @Override
    public String createRegisterToken(UserInfo userInfo) {
        UUID uuid = UUID.randomUUID();
        String token = uuid.toString();
        RegisterVerificationToken registerVerificationToken = new RegisterVerificationToken(
                token, userInfo.id(), System.currentTimeMillis(), false
        );

        return uuid.toString();
    }

    @Override
    public UserInfo verifyRegisterToken(String token) {
        return null;
    }
}
