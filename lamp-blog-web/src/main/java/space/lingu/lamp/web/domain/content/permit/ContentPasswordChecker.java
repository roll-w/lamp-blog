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
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentAccessCredential;

/**
 * Check content's password.
 *
 * @author RollW
 */
public class ContentPasswordChecker implements ContentPermitChecker {

    @Override
    @NonNull
    public ContentPermitResult checkPermit(@NonNull Content content, @NonNull ContentAccessCredential credential) {
        if (credential.getType() != ContentAccessAuthType.PASSWORD) {
            return ContentPermitResult.permit();
        }
        // TODO: add password check
        return ContentPermitResult.permit();
    }
}
