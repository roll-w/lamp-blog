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
import space.lingu.lamp.web.domain.authentication.token.TokenAuthResult;
import space.lingu.lamp.web.domain.authentication.common.AuthErrorCode;
import space.lingu.lamp.web.domain.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.web.domain.user.service.UserSignatureProvider;

/**
 * @author RollW
 */
@AuthApi
public class AuthTokenController {
    private final AuthenticationTokenService tokenService;
    private final UserSignatureProvider userSignatureProvider;

    public AuthTokenController(AuthenticationTokenService tokenService,
                               UserSignatureProvider userSignatureProvider) {
        this.tokenService = tokenService;
        this.userSignatureProvider = userSignatureProvider;
    }

    @PostMapping("/token/r")
    public HttpResponseEntity<String> refreshToken(
            @RequestParam(name = "token") String oldToken) {
        Long userId = tokenService.getUserId(oldToken);
        if (userId == null) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        String sig = userSignatureProvider.getSignature(userId);
        TokenAuthResult result = tokenService.verifyToken(oldToken, sig);
        if (result.state()) {
            return HttpResponseEntity.success(oldToken);
        }
        if (result.errorCode() == AuthErrorCode.ERROR_TOKEN_EXPIRED) {
            return HttpResponseEntity.success(
                    tokenService.generateAuthToken(result.userId(), sig)
            );
        }
        return HttpResponseEntity.of(AuthErrorCode.ERROR_INVALID_TOKEN);
    }

    @GetMapping("/token/v")
    public HttpResponseEntity<TokenAuthResult> verifyToken(
            @RequestParam String token) {
        Long userId = tokenService.getUserId(token);
        if (userId == null) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_INVALID_TOKEN);
        }
        String sig = userSignatureProvider.getSignature(userId);
        TokenAuthResult authResult = tokenService.verifyToken(token, sig);
        return HttpResponseEntity.of(
                        authResult.errorCode(), authResult);
    }
}
