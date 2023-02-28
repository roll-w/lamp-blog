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

import space.lingu.NonNull;
import space.lingu.lamp.web.domain.authentication.common.AuthErrorCode;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentAccessCredential;
import space.lingu.lamp.web.domain.content.ContentAccessCredentials;
import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.lamp.web.domain.user.common.UserErrorCode;

/**
 * @author RollW
 */
public class ContentUserChecker implements ContentPermitChecker {
    public ContentUserChecker() {
    }

    @Override
    @NonNull
    public ContentPermitResult checkAccessPermit(@NonNull Content content,
                                                 @NonNull ContentAccessAuthType contentAccessAuthType,
                                                 @NonNull ContentAccessCredentials credentials) {
        if (!contentAccessAuthType.needsAuth()) {
            return ContentPermitResult.permit();
        }
        ContentAccessCredential credential = credentials.getCredential(ContentAccessAuthType.USER);
        if (credential == null || credential.getRawData() == null) {
            return ContentPermitResult.deny(UserErrorCode.ERROR_USER_NOT_LOGIN);
        }

        if (contentAccessAuthType != ContentAccessAuthType.PRIVATE) {
            return ContentPermitResult.permit();
        }

        Type type = getType(credential.getRawData());
        return switch (type) {
            case LONG -> checkIfLong(content, (Long) credential.getRawData());
            case USER_IDENTITY -> checkIfUserIdentity(content, (UserIdentity) credential.getRawData());
        };
    }

    private ContentPermitResult checkIfLong(Content content, long userId) {
        if (content.getUserId() == userId) {
            return ContentPermitResult.permit();
        }
        return ContentPermitResult.deny(AuthErrorCode.ERROR_NOT_HAS_ROLE);
    }

    private ContentPermitResult checkIfUserIdentity(Content content,
                                                    UserIdentity user) {
        if (content.getUserId() == user.getUserId()) {
            return ContentPermitResult.permit();
        }
        return ContentPermitResult.deny(AuthErrorCode.ERROR_NOT_HAS_ROLE);
    }

    private Type getType(Object data) {
        if (data instanceof Long) {
            return Type.LONG;
        } else if (data instanceof UserIdentity) {
            return Type.USER_IDENTITY;
        }
        throw new IllegalArgumentException("Unknown type of data: " + data.getClass().getName());
    }

    /**
     * Supported types.
     *
     * @see ContentAccessAuthType#USER
     * @see ContentAccessAuthType#getTypes()
     */
    private enum Type {
        LONG,
        USER_IDENTITY
    }
}
