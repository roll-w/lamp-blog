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

/**
 * @author RollW
 */
public enum CommonErrorCode implements ErrorCode {
    SUCCESS(SUCCESS_CODE, 200),

    ERROR_UNKNOWN("A0000", 500),
    ERROR_NOT_FOUND("B0100", 404),
    ERROR_EXCEPTION("D0000", 500);

    private final String value;
    private final int status;

    CommonErrorCode(String value, int status) {
        this.value = value;
        this.status = status;
    }

    @NonNull
    @Override
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
