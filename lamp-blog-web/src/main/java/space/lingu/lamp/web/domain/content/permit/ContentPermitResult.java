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

package space.lingu.lamp.web.domain.content.permit;

import com.google.common.base.Preconditions;
import tech.rollw.common.web.ErrorCode;

import java.util.HashSet;
import java.util.Set;

/**
 * @author RollW
 */
public record ContentPermitResult(
        Set<ErrorCode> errors,
        boolean isPermitted
) {
    public static ContentPermitResult permit() {
        return new ContentPermitResult(Set.of(), true);
    }

    public static ContentPermitResult deny(ErrorCode errorCode) {
        Preconditions.checkArgument(errorCode.failed(), "errorCode must be failed.");
        return new ContentPermitResult(Set.of(errorCode), false);
    }

    public ContentPermitResult plus(ContentPermitResult other) {
        Preconditions.checkNotNull(other);
        Set<ErrorCode> pluses = new HashSet<>(errors);
        pluses.addAll(other.errors);
        return new ContentPermitResult(pluses, isPermitted && other.isPermitted);
    }

}
