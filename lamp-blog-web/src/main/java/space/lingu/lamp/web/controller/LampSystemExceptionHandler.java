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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCodeFinderChain;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.SystemRuntimeException;
import space.lingu.lamp.web.common.ParameterMissingException;
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
    private final ErrorCodeFinderChain codeFinderChain;

    public LampSystemExceptionHandler(ErrorCodeFinderChain codeFinderChain) {
        this.codeFinderChain = codeFinderChain;
    }


    @ExceptionHandler(LightRuntimeException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(LightRuntimeException e) {
        logger.error("Unexpected sql error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.failure(
                codeFinderChain.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(ParameterMissingException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(ParameterMissingException e) {
        logger.warn("Param missing: %s".formatted(e.toString()), e);
        return HttpResponseEntity.failure(
                codeFinderChain.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(SystemRuntimeException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(SystemRuntimeException e) {
        logger.error("System runtime error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.failure(
                codeFinderChain.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(NullPointerException e) {
        logger.error("Null exception : %s".formatted(e.toString()), e);
        return HttpResponseEntity.failure(
                CommonErrorCode.ERROR_NULL,
                e.getMessage()
        );
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(FileNotFoundException e) {
        return HttpResponseEntity.failure(
                CommonErrorCode.ERROR_NOT_FOUND,
                "404 Not found."
        );
    }

    @ExceptionHandler(IOException.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(IOException e) {
        logger.error("IO Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.failure(
                codeFinderChain.fromThrowable(e),
                e.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponseEntity<String> handle(Exception e) {
        logger.error("Error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.failure(
                codeFinderChain.fromThrowable(e),
                e.getMessage()
        );
    }
}
