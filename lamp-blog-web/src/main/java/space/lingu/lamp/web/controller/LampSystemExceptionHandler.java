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

import com.google.common.base.Strings;
import com.google.common.base.VerifyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import space.lingu.lamp.BusinessRuntimeException;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.ErrorCodeFinder;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.IoErrorCode;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.common.ParameterMissingException;
import space.lingu.lamp.web.common.WebCommonErrorCode;
import space.lingu.lamp.web.domain.authentication.common.AuthErrorCode;
import space.lingu.lamp.web.domain.authentication.login.LoginTokenException;
import space.lingu.lamp.web.system.ErrorRecord;
import space.lingu.lamp.web.system.ErrorRecordVo;
import space.lingu.light.LightRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingDeque;

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
    private final MessageSource messageSource;

    public LampSystemExceptionHandler(ErrorCodeFinder errorCodeFinder,
                                      MessageSource messageSource) {
        this.errorCodeFinder = errorCodeFinder;
        this.messageSource = messageSource;
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
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e)
        );
    }

    @ExceptionHandler(BusinessRuntimeException.class)
    public HttpResponseEntity<Void> handle(BusinessRuntimeException e) {
        logger.error("System runtime error: %s".formatted(e.toString()), e);
        return HttpResponseEntity.of(
                errorCodeFinder.fromThrowable(e),
                tryGetMessage(e)
        );
    }

    private String tryGetMessage(BusinessRuntimeException e) {
        if (Strings.isNullOrEmpty(e.getMessage())) {
            return null;
        }
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Locale locale = context == null ? Locale.getDefault() : context.locale();
        try {
            return messageSource.getMessage(
                    e.getMessage(),
                    e.getArgs(),
                    locale
            );
        } catch (Exception ex) {
            return tryFormatMessage(e.getMessage(), e.getArgs());
        }
    }

    private String tryFormatMessage(String message, Object[] args) {
        if (args == null || args.length == 0) {
            return message;
        }
        try {
            return MessageFormat.format(message, args);
        } catch (Exception e) {
            return message;
        }
    }

    @ExceptionHandler(NullPointerException.class)
    public HttpResponseEntity<Void> handle(NullPointerException e) {
        logger.error("Null exception : %s".formatted(e.toString()), e);
        recordErrorLog(CommonErrorCode.ERROR_NULL, e);
        return HttpResponseEntity.of(
                CommonErrorCode.ERROR_NULL,
                e.getMessage()
        );
    }

    // treat VerifyException as parameter failed
    @ExceptionHandler(VerifyException.class)
    public HttpResponseEntity<Void> handle(VerifyException e) {
        recordErrorLog(WebCommonErrorCode.ERROR_PARAM_FAILED, e);
        return HttpResponseEntity.of(
                WebCommonErrorCode.ERROR_PARAM_FAILED,
                e.getMessage()
        );
    }

    @ExceptionHandler(FileNotFoundException.class)
    public HttpResponseEntity<Void> handle(FileNotFoundException e) {
        recordErrorLog(IoErrorCode.ERROR_FILE_NOT_FOUND, e);
        return HttpResponseEntity.of(
                IoErrorCode.ERROR_FILE_NOT_FOUND,
                e.getMessage()
        );
    }

    @ExceptionHandler(IOException.class)
    public HttpResponseEntity<Void> handle(IOException e) {
        logger.error("IO Error: %s".formatted(e.toString()), e);
        ErrorRecord errorRecord = recordErrorLog(e);
        return HttpResponseEntity.of(
                errorRecord.errorCode(),
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
        ErrorRecord errorRecord = recordErrorLog(e);
        return HttpResponseEntity.of(
                errorRecord.errorCode(),
                e.getMessage()
        );
    }

    private final Deque<ErrorRecord> errorRecords = new LinkedBlockingDeque<>();

    private ErrorRecord recordErrorLog(Throwable throwable) {
        ErrorCode errorCode = errorCodeFinder.fromThrowable(throwable);
        return recordErrorLog(errorCode, throwable);
    }

    private ErrorRecord recordErrorLog(ErrorCode errorCode, Throwable throwable) {
        long time = System.currentTimeMillis();
        ErrorRecord errorRecord = new ErrorRecord(errorCode, throwable, time);
        putErrorRecord(errorRecord);
        return errorRecord;
    }

    @Async
    void putErrorRecord(ErrorRecord errorRecord) {
        errorRecords.addLast(errorRecord);
        if (errorRecords.size() > 100) {
            errorRecords.removeFirst();
        }
    }

    @GetMapping("/api/admin/error/records")
    public HttpResponseEntity<List<ErrorRecordVo>> getErrorRecords() {
        return HttpResponseEntity.success(
                errorRecords.stream()
                        .map(ErrorRecordVo::from)
                        .toList()
        );
    }
}
