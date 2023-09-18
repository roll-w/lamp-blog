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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import space.lingu.lamp.RequestMetadata;
import space.lingu.lamp.web.common.ParamValidate;
import space.lingu.lamp.web.controller.user.vo.LoginResponse;
import space.lingu.lamp.web.controller.user.vo.LoginTokenSendRequest;
import space.lingu.lamp.web.controller.user.vo.UserLoginRequest;
import space.lingu.lamp.web.controller.user.vo.UserRegisterRequest;
import space.lingu.lamp.web.domain.authentication.login.LoginStrategyType;
import space.lingu.lamp.web.domain.authentication.token.AuthenticationTokenService;
import space.lingu.lamp.web.domain.user.AttributedUser;
import space.lingu.lamp.web.domain.user.dto.UserInfoSignature;
import space.lingu.lamp.web.domain.user.service.LoginRegisterService;
import tech.rollw.common.web.HttpResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author RollW
 */
@UserApi
public class LoginRegisterController {
    private static final Logger logger = LoggerFactory.getLogger(LoginRegisterController.class);

    private final LoginRegisterService loginRegisterService;
    private final AuthenticationTokenService authenticationTokenService;

    public LoginRegisterController(LoginRegisterService loginRegisterService,
                                   AuthenticationTokenService authenticationTokenService) {
        this.loginRegisterService = loginRegisterService;
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/login/password")
    public HttpResponseEntity<LoginResponse> loginByPassword(
            RequestMetadata requestMetadata,
            @RequestBody UserLoginRequest loginRequest) {
        // account login, account maybe the username or email
        // needs to check the account type and get the user id
        ParamValidate.notEmpty(loginRequest.identity(), "identity cannot be null or empty.");
        ParamValidate.notEmpty(loginRequest.token(), "token cannot be null or empty.");

        UserInfoSignature userInfoSignature = loginRegisterService.loginUser(
                loginRequest.identity(),
                loginRequest.token(),
                requestMetadata,
                LoginStrategyType.PASSWORD);
        return loginResponse(userInfoSignature);
    }

    // TODO: login by email token
    @PostMapping("/login/token/email")
    public HttpResponseEntity<LoginResponse> loginByEmailToken(
            RequestMetadata requestMetadata,
            @RequestBody UserLoginRequest loginRequest) {
        UserInfoSignature signature = loginRegisterService.loginUser(
                loginRequest.identity(),
                loginRequest.token(),
                requestMetadata,
                LoginStrategyType.EMAIL_TOKEN);
        return loginResponse(signature);
    }

    @PostMapping("/login/token")
    public HttpResponseEntity<Void> sendEmailLoginToken(
            HttpServletRequest request,
            @RequestBody LoginTokenSendRequest loginTokenSendRequest) throws IOException {
        ParamValidate.notEmpty(loginTokenSendRequest.identity(), "identity cannot be null or empty.");
        loginRegisterService.sendToken(
                loginTokenSendRequest.identity(),
                LoginStrategyType.EMAIL_TOKEN
        );
        return HttpResponseEntity.success();
    }

    private HttpResponseEntity<LoginResponse> loginResponse(UserInfoSignature userInfoSignature) {
        String token = authenticationTokenService.generateAuthToken(
                userInfoSignature.id(),
                userInfoSignature.signature()
        );
        LoginResponse response = new LoginResponse(
                token,
                userInfoSignature
        );
        return HttpResponseEntity.success(response);
    }

    @PostMapping("/register")
    public HttpResponseEntity<Void> registerUser(@RequestBody UserRegisterRequest request) {
        AttributedUser user = loginRegisterService.registerUser(
                request.username(),
                request.password(),
                request.email()
        );
        return HttpResponseEntity.success();
    }

    @PostMapping("/register/token/{token}")
    public HttpResponseEntity<Void> activateUser(@PathVariable String token) {
        ParamValidate.notEmpty(token, "Token cannot be null or empty.");
        loginRegisterService.verifyRegisterToken(token);
        return HttpResponseEntity.success();
    }

    @PostMapping(value = "/register/token")
    public HttpResponseEntity<Void> resendRegisterToken(
            @RequestParam("username") String username) {
        ParamValidate.notEmpty(username,
                "Username cannot be null or empty.");
        loginRegisterService.resendToken(username);

        return HttpResponseEntity.success();
    }

    @PostMapping("/logout")
    public HttpResponseEntity<Void> logout(HttpServletRequest request) {
        return HttpResponseEntity.success();
    }
}
