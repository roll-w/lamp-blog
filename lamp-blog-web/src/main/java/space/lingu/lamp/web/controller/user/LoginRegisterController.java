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

package space.lingu.lamp.web.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.MessagePackage;
import space.lingu.lamp.web.authentication.login.LoginStrategyType;
import space.lingu.lamp.web.data.dto.user.LoginResponse;
import space.lingu.lamp.web.data.dto.user.LoginTokenSendRequest;
import space.lingu.lamp.web.data.dto.user.UserInfo;
import space.lingu.lamp.web.data.dto.user.UserLoginRequest;
import space.lingu.lamp.web.data.dto.user.UserRegisterRequest;
import space.lingu.lamp.web.data.entity.user.Role;
import space.lingu.lamp.web.service.auth.AuthenticationTokenService;
import space.lingu.lamp.web.service.user.LoginRegisterService;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@UserApi
public class LoginRegisterController {
    private final LoginRegisterService loginRegisterService;
    private final AuthenticationTokenService authenticationTokenService;

    public LoginRegisterController(LoginRegisterService loginRegisterService,
                                   AuthenticationTokenService authenticationTokenService) {
        this.loginRegisterService = loginRegisterService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/login/password")
    public HttpResponseEntity<LoginResponse> loginByPassword(
            HttpServletRequest request,
            @RequestBody UserLoginRequest loginRequest) {
        // account login, account maybe the username or email
        // needs to check the account type and get the user id
        MessagePackage<UserInfo> res = loginRegisterService.loginUser(
                loginRequest.identity(),
                loginRequest.token(),
                LoginStrategyType.PASSWORD);
        if (!res.errorCode().getState()) {
            return HttpResponseEntity.create(
                    res.toResponseBody(() -> LoginResponse.NULL)
            );
        }
        String token = authenticationTokenService.generateAuthToken(
                res.data().id()
        );
        LoginResponse response = new LoginResponse(token, res.data());
        return HttpResponseEntity.success(response);
    }

    // TODO: login by email token
    @PostMapping("/login/email")
    public HttpResponseEntity<LoginResponse> loginByEmailToken(
            HttpServletRequest request,
            @RequestBody UserLoginRequest loginRequest) {
        return null;
    }

    @PostMapping("/login/email/token")
    public HttpResponseEntity<Void> sendEmailLoginToken(
            HttpServletRequest request,
            @RequestBody LoginTokenSendRequest loginTokenSendRequest) {
        return null;
    }

    @PostMapping("/register")
    public HttpResponseEntity<Void> registerUser(@RequestBody UserRegisterRequest request) {
        MessagePackage<UserInfo> res = loginRegisterService.registerUser(
                request.username(),
                request.password(),
                request.email(), Role.USER
        );
        return HttpResponseEntity.create(res.toResponseBody(() -> null));
    }

    @PostMapping("/logout")
    public HttpResponseEntity<Void> logout() {

        return HttpResponseEntity.success();
    }
}
