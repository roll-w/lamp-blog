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

package space.lingu.lamp;

import space.lingu.NonNull;

import java.text.MessageFormat;

/**
 * Business Runtime Exception. Can be used to pass user prompts into responses.
 * If it is an internal system error, use other Exception classes.
 * <p>
 * Avoid using this class directly to throw exception
 * and try to use inherited classes instead.
 *
 * @author RollW
 */
public class BusinessRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
    private final Object[] args;

    public BusinessRuntimeException(ErrorCode errorCode) {
        this(errorCode, errorCode.toString());
    }

    public BusinessRuntimeException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode.toString());
        this.errorCode = errorCode;
        this.message = message;
        this.args = args;
    }

    public BusinessRuntimeException(ErrorCode errorCode, String message, Throwable cause, Object... args) {
        super(message, cause);
        this.message = message;
        this.errorCode = errorCode;
        this.args = args;
    }

    public BusinessRuntimeException(Throwable cause) {
        this(ErrorCodeFinderChain.start(), cause);
    }

    public BusinessRuntimeException(ErrorCodeFinder errorCodeFinder, Throwable cause) {
        this(errorCodeFinder, cause, cause.toString());
    }

    public BusinessRuntimeException(ErrorCodeFinder codeFinderChain, Throwable cause, String message, Object... args) {
        super(cause);
        this.errorCode = codeFinderChain.fromThrowable(cause);
        this.message = message;
        this.args = args;
    }

    public BusinessRuntimeException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = cause.toString();
        this.args = new Object[0];
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @NonNull
    public Object[] getArgs() {
        return args;
    }

    public String getFormattedMessage() {
        if (args == null || args.length == 0) {
            return message;
        }
        try {
            return MessageFormat.format(message, args);
        } catch (Exception e) {
            return message;
        }
    }
}
