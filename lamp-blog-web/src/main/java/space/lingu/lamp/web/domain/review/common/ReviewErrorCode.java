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

package space.lingu.lamp.web.domain.review.common;

import space.lingu.NonNull;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.ErrorCodeFinder;
import tech.rollw.common.web.ErrorCodeMessageProvider;

import java.util.List;


/**
 * @author RollW
 */
public enum ReviewErrorCode implements ErrorCode, ErrorCodeFinder, ErrorCodeMessageProvider {
    ERROR_REVIEW("A2100", 400),
    ERROR_REVIEWED("A2101", 400),
    ERROR_NOT_REVIEWED("A2102", 400),
    ;


    private final String value;
    private final int status;

    ReviewErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    ReviewErrorCode(String value) {
        this.value = value;
        this.status = 400;
    }

    @Override
    public String toString() {
        return "ReviewError: %s, code: %s".formatted(name(), getCode());
    }

    @Override
    @NonNull
    public String getCode() {
        return value;
    }

    @NonNull
    @Override
    public String getName() {
        return name();
    }

    @Override
    public boolean success() {
        return false;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public ErrorCode fromThrowable(Throwable e, ErrorCode defaultErrorCode) {
        return null;
    }

    @Override
    public ErrorCode findErrorCode(String codeValue) {
        return ErrorCodeFinder.from(values(), codeValue);
    }

    private static final List<ErrorCode> CODES = List.of(values());

    @Override
    public List<ErrorCode> listErrorCodes() {
        return CODES;
    }

    public static ErrorCodeFinder getFinderInstance() {
        return ERROR_REVIEW;
    }
}
