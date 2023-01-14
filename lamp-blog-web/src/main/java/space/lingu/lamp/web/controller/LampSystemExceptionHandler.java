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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCodeFinder;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.SystemRuntimeException;
import space.lingu.lamp.web.authentication.login.LoginTokenException;
import space.lingu.lamp.web.common.ParameterMissingException;
import space.lingu.lamp.web.service.auth.AuthErrorCode;
import space.lingu.light.LightRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Handle {@link Exception}s
 *
 * @author RollW
 */
@ControllerAdvice
@RestController
public class LampSystemExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(LampSystemExceptionHandler.class);
    private final ErrorCodeFinder errorCodeFinder;

    public LampSystemExceptionHandler(ErrorCodeFinder errorCodeFinder) {
        this.errorCodeFinder = errorCodeFinder;
    }


    @ExceptionHandler(LightRuntimeException.class)
    public HttpResponseEntity<Void> handle(LightRuntimeException e) {
        logger.error("Unexpected sql error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(ParameterMissingException.class)
    public HttpResponseEntity<Void> handle(ParameterMissingException e) {
        logger.warn("Param missing: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(LoginTokenException.class)
    public HttpResponseEntity<Void> handle(LoginTokenException e) {
        logger.error("Token exception: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e)
        );
    }

    @ExceptionHandler(SystemRuntimeException.class)
    public HttpResponseEntity<Void> handle(SystemRuntimeException e) {
        logger.error("System runtime error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e)
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpResponseEntity<Void> handle(NullPointerException e) {
        logger.error("Null exception : %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                CommonErrorCode.ERROR_NULL,
                e.getMessage()
        );
    }

    @ExceptionHandler(FileNotFoundException.class)
    public HttpResponseEntity<Void> handle(FileNotFoundException e) {
        return HttpResponseEntity.of(
                CommonErrorCode.ERROR_NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(IOException.class)
    public HttpResponseEntity<Void> handle(IOException e) {
        logger.error("IO Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public HttpResponseEntity<Void> handleAuthException(AccessDeniedException e) {
        return HttpResponseEntity.of(AuthErrorCode.ERROR_UNAUTHORIZED_USE);
    }

    @ExceptionHandler(AuthenticationException.class)
    public HttpResponseEntity<Void> handleAuthException(AuthenticationException e) {
        return HttpResponseEntity.of(AuthErrorCode.ERROR_NOT_HAS_ROLE);
    }


    @ExceptionHandler(Exception.class)
    public HttpResponseEntity<Void> handle(Exception e) {
        logger.error("Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                e.getMessage()
        );
    }
}
