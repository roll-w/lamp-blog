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

package tech.lamprism.lampray.content.permit

import tech.lamprism.lampray.content.Content
import tech.lamprism.lampray.content.ContentAccessAuthType
import tech.lamprism.lampray.content.ContentAccessCredentials
import tech.rollw.common.web.ErrorCode

/**
 * @author RollW
 */
interface ContentPermitCheckProvider {
    fun supports(contentAccessAuthType: ContentAccessAuthType): Boolean

    fun checkAccessPermit(
        content: Content,
        contentAccessAuthType: ContentAccessAuthType,
        credentials: ContentAccessCredentials
    ): ErrorCode
}