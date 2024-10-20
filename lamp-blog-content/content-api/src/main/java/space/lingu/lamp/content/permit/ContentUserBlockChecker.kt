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
package space.lingu.lamp.content.permit

import org.springframework.stereotype.Component
import space.lingu.lamp.content.Content
import space.lingu.lamp.content.ContentAccessAuthType
import space.lingu.lamp.content.ContentAccessCredential
import space.lingu.lamp.content.ContentAccessCredentials
import space.lingu.lamp.user.UserProvider
import space.lingu.lamp.user.UserTrait
import space.lingu.lamp.user.UserViewException
import tech.rollw.common.web.UserErrorCode

/**
 * Check if the user is blocked or disabled
 * to access the content
 *
 * @author RollW
 */
@Component
class ContentUserBlockChecker(
    private val userProvider: UserProvider
) : ContentPermitCheckProvider {
    override fun supports(
        contentAccessAuthType: ContentAccessAuthType
    ): Boolean = true

    override fun checkAccessPermit(
        content: Content,
        contentAccessAuthType: ContentAccessAuthType,
        credentials: ContentAccessCredentials
    ): ContentPermitResult {
        val credential = credentials
            .getCredential(ContentAccessAuthType.USER)
            ?: return ContentPermitResult.permit()

        val creatorId = content.userId
        val accessUserId = tryGetUserId(credential)
        if (creatorId == accessUserId) {
            return ContentPermitResult.permit()
        }
        try {
            val user = userProvider.getUser(accessUserId)
            if (!user.isNormal) {
                return ContentPermitResult.deny(UserErrorCode.ERROR_USER_DISABLED)
            }
        } catch (e: UserViewException) {
            return ContentPermitResult.deny(e.errorCode)
        }
        return ContentPermitResult.permit()
    }

    private fun tryGetUserId(credential: ContentAccessCredential): Long {
        val data = credential.rawData ?: throw IllegalArgumentException("Credential data should not be null")
        if (data is Long) {
            return data
        }
        if (data is UserTrait) {
            return data.userId
        }
        throw IllegalArgumentException("Invalid credential data type: " + data.javaClass.name)
    }
}
