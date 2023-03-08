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

package space.lingu.lamp.web.domain.user.common;

import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.ErrorCodeFinder;
import space.lingu.lamp.ErrorCodeMessageProvider;

import java.util.List;

/**
 * User Error Code.
 * <p>
 * From A0000 to A0999.
 *
 * @author RollW
 */
public enum UserErrorCode implements ErrorCode, ErrorCodeFinder, ErrorCodeMessageProvider {
    SUCCESS(SUCCESS_CODE, 200),
    /**
     * 注册错误
     */
    ERROR_REGISTER("A0000", 400),
    /**
     * 用户已存在
     */
    ERROR_USER_EXISTED("A0001", 400),
    /**
     * 用户已登录
     */
    ERROR_USER_ALREADY_LOGIN("A0002", 400),
    /**
     * 用户已激活
     */
    ERROR_USER_ALREADY_ACTIVATED("A0003", 400),
    /**
     * 用户不存在
     */
    ERROR_USER_NOT_EXIST("A0004", 404),
    /**
     * 用户未登录
     */
    ERROR_USER_NOT_LOGIN("A0005", 401),
    ERROR_USER_DISABLED("A0006", 403),
    /**
     * 用户已注销
     */
    ERROR_USER_CANCELED("A0007", 403),
    /**
     * 登陆状态过期
     */
    ERROR_LOGIN_EXPIRED("A0008", 401),
    /**
     * 密码错误
     */
    ERROR_PASSWORD_NOT_CORRECT("A0010", 400),
    /**
     * 密码不合规，校验错误
     */
    ERROR_PASSWORD_NON_COMPLIANCE("A0011", 400),
    /**
     * 用户名不合规
     */
    ERROR_USERNAME_NON_COMPLIANCE("A0012", 400),
    ERROR_USERNAME_SENSITIVE("A0013", 400),
    /**
     * 邮件名不合规
     */
    ERROR_EMAIL_NON_COMPLIANCE("A0014", 400),

    ERROR_EMAIL_EXISTED("A0015", 400),

    ;


    private final String value;
    private final int status;

    UserErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @Override
    public String toString() {
        if (this == SUCCESS) {
            return "SUCCESS";
        }

        return "UserError: %s, code: %s".formatted(name(), getCode());
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
