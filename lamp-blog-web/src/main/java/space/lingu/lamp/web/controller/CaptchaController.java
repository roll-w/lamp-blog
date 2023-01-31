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

package space.lingu.lamp.web.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author RollW
 */
@RestController
@CommonApi
public class CaptchaController {
    private static final String CAPTCHA_COOKIE_NAME = "captack_id";


    @GetMapping("/captcha")
    public String getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (CAPTCHA_COOKIE_NAME.equals(cookie.getName())) {
                return "OK";
            }
        }

        Cookie cookie = new Cookie(CAPTCHA_COOKIE_NAME, RandomStringUtils.randomAlphanumeric(32));
        response.addCookie(cookie);
        return "OK";
    }
}
