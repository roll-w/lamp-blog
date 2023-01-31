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

package space.lingu.lamp.web.domain.authentication.common;

import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.ErrorCodeFinder;

import java.util.List;

/**
 * Authentication & Authorization Error Code.
 * <p>
 * From A1000 to A1049.
 *
 * @author RollW
 */
public enum AuthErrorCode implements ErrorCode {
    SUCCESS(SUCCESS_CODE, 200),

    /**
     * 认证错误
     */
    ERROR_AUTH("A1000", 401),
    /**
     * 无效令牌
     */
    ERROR_INVALID_TOKEN("A1001", 401),
    /**
     * 令牌过期
     */
    ERROR_TOKEN_EXPIRED("A1002", 401),
    /**
     * 令牌未过期
     */
    ERROR_TOKEN_NOT_EXPIRED("A1003", 401),
    ERROR_TOKEN_EXISTED("A1004", 401),
    ERROR_TOKEN_NOT_EXIST("A1005", 401),
    ERROR_TOKEN_NOT_MATCH("A1006", 401),
    ERROR_TOKEN_USED("A1007", 401),
    ERROR_TOKEN_NOT_USED("A1008", 401),
    /**
     * 未认证
     */
    ERROR_UNAUTHORIZED_USE("A1009", 403),
    /**
     * 无权限访问
     */
    ERROR_NOT_HAS_ROLE("A1010", 401),
    /**
     * 黑名单用户
     */
    ERROR_IN_BLOCKLIST("A1011", 401),
    ;


    private final String value;
    private final int status;

    AuthErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        if (this == SUCCESS) {
            return "SUCCESS";
        }

        return "AuthError: %s, code: %s".formatted(name(), getCode());
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
        return this == SUCCESS;
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
        return SUCCESS;
    }
}
