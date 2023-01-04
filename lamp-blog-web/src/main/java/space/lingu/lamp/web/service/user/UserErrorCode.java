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

package space.lingu.lamp.web.service.user;

import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.ErrorCodeFinder;

/**
 * User Error Code.
 * <p>
 * From A0000 to A0999.
 *
 * @author RollW
 */
public enum UserErrorCode implements ErrorCode {
    SUCCESS(SUCCESS_CODE, 200),
    /**
     * 注册错误
     */
    ERROR_REGISTER("A0000"),
    /**
     * 用户已存在
     */
    ERROR_USER_EXISTED("A0001"),
    /**
     * 用户已登录
     */
    ERROR_USER_ALREADY_LOGIN("A0002"),
    /**
     * 用户已激活
     */
    ERROR_USER_ALREADY_ACTIVATED("A0003"),
    /**
     * 用户不存在
     */
    ERROR_USER_NOT_EXIST("A0004"),
    /**
     * 用户未登录
     */
    ERROR_USER_NOT_LOGIN("A0005", 401),
    /**
     * 用户已注销
     */
    ERROR_USER_CANCELED("A0006", 401),
    /**
     * 登陆状态过期
     */
    ERROR_LOGIN_EXPIRED("A0007"),
    /**
     * 密码错误
     */
    ERROR_PASSWORD_NOT_CORRECT("A0010"),
    /**
     * 密码不合规，校验错误
     */
    ERROR_PASSWORD_NON_COMPLIANCE("A0011"),
    /**
     * 用户名不合规
     */
    ERROR_USERNAME_NON_COMPLIANCE("A0012"),
    /**
     * 邮件名不合规
     */
    ERROR_EMAIL_NON_COMPLIANCE("A0013"),

    ERROR_EMAIL_EXISTED("A0014");


    private final String value;
    private final int status;

    UserErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    UserErrorCode(String value) {
        this.value = value;
        this.status = 403;
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
    public boolean getState() {
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

    public static ErrorCodeFinder getFinderInstance() {
        return SUCCESS;
    }
}
