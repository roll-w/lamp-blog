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

package space.lingu.lamp.content.article.common;

import space.lingu.NonNull;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.ErrorCodeFinder;
import tech.rollw.common.web.ErrorCodeMessageProvider;

import java.util.List;

/**
 * @author RollW
 */
public enum ArticleErrorCode implements ErrorCode, ErrorCodeFinder, ErrorCodeMessageProvider {
    ERROR_ARTICLE("A2300"),
    ERROR_TITLE_EMPTY("A2301", 400),
    ERROR_TITLE_TOO_LONG("A2302", 400),
    ERROR_TITLE_TOO_SHORT("A2303", 400),
    ERROR_CONTENT_EMPTY("A2304", 400),
    ERROR_CONTENT_TOO_LONG("A2305", 400),
    ERROR_CONTENT_TOO_SHORT("A2306", 400),

    ;


    private final String value;
    private final int status;

    ArticleErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    ArticleErrorCode(String value) {
        this.value = value;
        this.status = 403;
    }

    @Override
    public String toString() {
        return "ArticleError: %s, code: %s".formatted(name(), getCode());
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
        return ERROR_ARTICLE;
    }
}
