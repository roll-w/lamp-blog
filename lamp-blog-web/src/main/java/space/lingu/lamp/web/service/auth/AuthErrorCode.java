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

package space.lingu.lamp.web.service.auth;

import space.lingu.NonNull;
import space.lingu.lamp.ErrorCode;

/**
 * @author RollW
 */
public enum AuthErrorCode implements ErrorCode {
    SUCCESS(SUCCESS_CODE, 200),

    INVALID_TOKEN("A1001", 401),
    ;


    private final String value;
    private final int status;

    AuthErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @Override
    @NonNull
    public String getCode() {
        return value;
    }

    @Override
    public boolean getState() {
        return this == SUCCESS;
    }

    @Override
    public int getStatus() {
        return status;
    }
}
