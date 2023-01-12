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

package space.lingu.lamp.web.common;

import org.springframework.web.bind.MissingServletRequestParameterException;
import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.ErrorCodeFinder;
import space.lingu.lamp.SystemRuntimeException;

/**
 * @author RollW
 */
public enum WebCommonErrorCode implements ErrorCode, ErrorCodeFinder {
    ERROR_PARAM_MISSING("A0201", 404),

    ;


    private final String value;
    private final int status;

    WebCommonErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        return "WebCommonError: %s, code: %s".formatted(name(), getCode());
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
        return false;
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
        if (e instanceof MissingServletRequestParameterException) {
            return ERROR_PARAM_MISSING;
        }

        return null;
    }

    @Override
    public ErrorCode findErrorCode(String codeValue) {
        return ErrorCodeFinder.from(values(), codeValue);
    }

    public static ErrorCodeFinder getFinderInstance() {
        return ERROR_PARAM_MISSING;
    }
}