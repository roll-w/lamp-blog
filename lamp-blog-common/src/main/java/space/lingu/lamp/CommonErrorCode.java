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
import space.lingu.Nullable;

/**
 * @author RollW
 */
public enum CommonErrorCode implements ErrorCode, ErrorCodeFinder {
    SUCCESS(SUCCESS_CODE, 200),
    ERROR_NOT_FOUND("B0100", 404),
    ERROR_EXCEPTION("D0000", 500),
    ERROR_UNKNOWN("F0000", 500),
    ;

    private final String value;
    private final int status;

    CommonErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        if (this == SUCCESS) {
            return "SUCCESS";
        }

        return "CommonError: %s, code: %s".formatted(name(), getCode());
    }

    @NonNull
    @Override
    public String getCode() {
        return value;
    }

    @NonNull
    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean getState() {
        return this == SUCCESS;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public ErrorCode fromThrowable(Throwable e, ErrorCode defaultErrorCode) {
        if (e instanceof SystemRuntimeException sys) {
            return sys.getErrorCode();
        }

        return null;
    }

    @Override
    public ErrorCode findErrorCode(String codeValue) {
        return ErrorCodeFinder.from(values(), codeValue);
    }

    @Nullable
    private static CommonErrorCode nullableFrom(String value) {
        for (CommonErrorCode errorCode : values()) {
            if (errorCode.value.equals(value)) {
                return errorCode;
            }
        }
        return null;
    }

    @NonNull
    public static CommonErrorCode from(String value) {
        CommonErrorCode errorCode = nullableFrom(value);
        if (errorCode == null) {
            return ERROR_UNKNOWN;
        }
        return errorCode;
    }
}
