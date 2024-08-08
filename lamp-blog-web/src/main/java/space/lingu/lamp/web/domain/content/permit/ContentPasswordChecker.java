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

import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentAccessCredential;
import space.lingu.lamp.web.domain.content.ContentAccessCredentials;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;

/**
 * Check content's password.
 *
 * @author RollW
 */
@Component
public class ContentPasswordChecker implements ContentPermitCheckProvider {

    @Override
    @NonNull
    public ContentPermitResult checkAccessPermit(@NonNull Content content,
                                                 @NonNull ContentAccessAuthType contentAccessAuthType,
                                                 @NonNull ContentAccessCredentials credentials) {
        if (contentAccessAuthType != ContentAccessAuthType.PASSWORD) {
            return ContentPermitResult.permit();
        }
        ContentAccessCredential credential = credentials.getCredential(ContentAccessAuthType.PASSWORD);
        if (credential == null) {
            return ContentPermitResult.deny(ContentErrorCode.ERROR_PASSWORD_REQUIRED);
        }
        // TODO: add password check
        return ContentPermitResult.permit();
    }

    @Override
    public boolean supports(@NonNull ContentAccessAuthType contentAccessAuthType) {
        return contentAccessAuthType == ContentAccessAuthType.PASSWORD;
    }
}
