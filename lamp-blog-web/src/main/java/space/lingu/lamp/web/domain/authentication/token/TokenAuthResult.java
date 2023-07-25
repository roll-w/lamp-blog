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

package space.lingu.lamp.web.domain.authentication.token;

import space.lingu.NonNull;
import tech.rollw.common.web.AuthErrorCode;

/**
 * @author RollW
 */
public record TokenAuthResult(
        long userId,
        String token,

        @NonNull
        AuthErrorCode errorCode) {
    public boolean success() {
        return errorCode.success();
    }

    public static TokenAuthResult success(long userId, String token) {
        return new TokenAuthResult(userId, token, AuthErrorCode.SUCCESS);
    }

    public static TokenAuthResult failure(AuthErrorCode errorCode) {
        return new TokenAuthResult(-1, null, errorCode);
    }
}
