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

package space.lingu.lamp.web.controller.auth;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.web.data.dto.TokenAuthResult;
import space.lingu.lamp.web.service.auth.AuthErrorCode;
import space.lingu.lamp.web.service.auth.AuthenticationTokenService;

/**
 * @author RollW
 */
@AuthApi
public class AuthTokenController {
    private final AuthenticationTokenService tokenService;

    public AuthTokenController(AuthenticationTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token/r")
    public HttpResponseEntity<String> refreshToken(
            @RequestParam(name = "token") String oldToken) {
        TokenAuthResult result = tokenService.verifyToken(oldToken);
        if (result.state()) {
            return HttpResponseEntity.success(oldToken);
        }
        if (result.errorCode() == AuthErrorCode.ERROR_TOKEN_EXPIRED) {
            return HttpResponseEntity.success(
                    tokenService.generateAuthToken(result.userId())
            );
        }
        return HttpResponseEntity.of(AuthErrorCode.ERROR_INVALID_TOKEN);
    }

    @GetMapping("/token/v")
    public HttpResponseEntity<TokenAuthResult> verifyToken(
            @RequestParam String token) {
        TokenAuthResult authResult = tokenService.verifyToken(token);
        return HttpResponseEntity.of(
                        authResult.errorCode(), authResult);
    }
}
