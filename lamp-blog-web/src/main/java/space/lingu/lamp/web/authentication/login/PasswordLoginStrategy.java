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

package space.lingu.lamp.web.authentication.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.web.data.dto.LoginPasswordToken;
import space.lingu.lamp.web.data.entity.user.User;
import space.lingu.lamp.web.data.entity.LoginVerifiableToken;
import space.lingu.lamp.web.service.auth.AuthErrorCode;

/**
 * @author RollW
 */
@Component
public class PasswordLoginStrategy implements LoginStrategy {
    private static final Logger logger = LoggerFactory.getLogger(PasswordLoginStrategy.class);

    @Override
    public LoginVerifiableToken createToken(User user) {
        return new LoginPasswordToken(null, user.getId());
    }

    @Override
    public ErrorCode verify(LoginVerifiableToken token) {
        if (!(token instanceof LoginPasswordToken)) {
            logger.error("Passed token type error: {}.", token.getClass().getName());
            return AuthErrorCode.ERROR_INVALID_TOKEN;
        }

        return null;
    }

    @Override
    public void sendToken(LoginVerifiableToken token) {
        // no need to send token
    }

    @Override
    public LoginStrategyType getStrategyType() {
        return LoginStrategyType.PASSWORD;
    }
}
