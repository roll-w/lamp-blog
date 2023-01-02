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

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.MessagePackage;
import space.lingu.lamp.web.authentication.login.LoginStrategy;
import space.lingu.lamp.web.authentication.login.LoginStrategyType;
import space.lingu.lamp.web.data.database.repository.UserRepository;
import space.lingu.lamp.web.data.dto.user.UserInfo;
import space.lingu.lamp.web.data.entity.LoginVerifiableToken;
import space.lingu.lamp.web.data.entity.user.User;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author RollW
 */
@Service
public class LoginService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Map<LoginStrategyType, LoginStrategy> loginStrategyMap =
            new EnumMap<>(LoginStrategyType.class);

    public LoginService(@NonNull List<LoginStrategy> strategies,
                        UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
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

}
