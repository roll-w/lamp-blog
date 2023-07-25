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

package space.lingu.lamp.web.domain.user.filter;

import space.lingu.NonNull;
import space.lingu.lamp.web.domain.user.common.UserChecker;
import tech.rollw.common.web.CommonErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.UserErrorCode;

/**
 * @author RollW
 */
public class UserInfoFormatValidateFilter implements UserInfoFilter {
    @NonNull
    @Override
    public ErrorCode filter(@NonNull UserFilteringInfo userFilteringInfo) {
        return switch (userFilteringInfo.filterType()) {
            case USERNAME -> UserChecker.checkUsername(userFilteringInfo.value())
                    ? CommonErrorCode.SUCCESS
                    : UserErrorCode.ERROR_USERNAME_NON_COMPLIANCE;
            case PASSWORD -> UserChecker.checkPassword(userFilteringInfo.value())
                    ? CommonErrorCode.SUCCESS
                    : UserErrorCode.ERROR_PASSWORD_NON_COMPLIANCE;
            case EMAIL -> UserChecker.checkEmail(userFilteringInfo.value())
                    ? CommonErrorCode.SUCCESS
                    : UserErrorCode.ERROR_EMAIL_NON_COMPLIANCE;
            // TODO: phone number & other check
            default -> CommonErrorCode.SUCCESS;
        };
    }
}
