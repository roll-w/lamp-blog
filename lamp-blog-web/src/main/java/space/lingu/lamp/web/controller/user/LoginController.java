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
import org.springframework.web.bind.annotation.RestController;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.web.data.dto.LoginResponse;
import space.lingu.lamp.web.data.dto.UserLoginRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RollW
 */
@RestController
@UserApi
public class LoginController {

    @PostMapping("/login/password")
    public HttpResponseEntity<LoginResponse> loginByPassword(
            HttpServletRequest request,
            @RequestBody UserLoginRequest loginRequest) {
        return null;
    }

}
