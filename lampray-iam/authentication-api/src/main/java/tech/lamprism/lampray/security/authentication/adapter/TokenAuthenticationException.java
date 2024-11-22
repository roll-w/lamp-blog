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

package tech.lamprism.lampray.security.authentication.adapter;

import org.springframework.security.core.AuthenticationException;
import tech.rollw.common.web.CommonRuntimeException;
import tech.rollw.common.web.ErrorCode;

/**
 * @author RollW
 */
public class TokenAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;

    public TokenAuthenticationException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public TokenAuthenticationException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public TokenAuthenticationException(CommonRuntimeException cause) {
        super(cause.getMessage(), cause);
        this.errorCode = cause.getErrorCode();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
