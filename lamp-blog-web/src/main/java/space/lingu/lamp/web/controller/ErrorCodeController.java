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

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.ErrorCodeFinder;
import tech.rollw.common.web.ErrorCodeMessageProvider;

import java.util.Locale;

/**
 * @author RollW
 */
@RestController
@CommonApi
public class ErrorCodeController {
    private final ErrorCodeMessageProvider errorCodeMessageProvider;
    private final ErrorCodeFinder errorCodeFinder;

    public ErrorCodeController(ErrorCodeMessageProvider errorCodeMessageProvider,
                               ErrorCodeFinder errorCodeFinder) {
        this.errorCodeMessageProvider = errorCodeMessageProvider;
        this.errorCodeFinder = errorCodeFinder;
    }

    @GetMapping("/code")
    public String getErrorCodeName(@RequestParam String code) {
        return errorCodeFinder.findErrorCode(code).getName();
    }

    @GetMapping("/code/message")
    public String getErrorCodeMessage(@RequestParam String code) {
        Locale locale = LocaleContextHolder.getLocale();
        ErrorCode errorCode = errorCodeFinder.findErrorCode(code);
        return errorCodeMessageProvider.getMessage(errorCode, locale);
    }
}
