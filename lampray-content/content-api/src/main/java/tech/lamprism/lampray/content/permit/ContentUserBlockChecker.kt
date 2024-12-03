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

import org.springframework.stereotype.Component
import tech.lamprism.lampray.content.Content
import tech.lamprism.lampray.content.ContentAccessAuthType
import tech.lamprism.lampray.content.ContentAccessCredential
import tech.lamprism.lampray.content.ContentAccessCredentials
import tech.lamprism.lampray.user.UserProvider
import tech.lamprism.lampray.user.UserTrait
import tech.lamprism.lampray.user.UserViewException
import tech.rollw.common.web.CommonErrorCode
import tech.rollw.common.web.ErrorCode
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
    ): ErrorCode {
        val credential = credentials
            .getCredential(ContentAccessAuthType.USER) ?: return CommonErrorCode.SUCCESS

        val creatorId = content.userId
        val accessUserId = tryGetUserId(credential)
        if (creatorId == accessUserId) {
            return CommonErrorCode.SUCCESS
        }
        return try {
            val user = userProvider.getUser(accessUserId)
            if (!user.isNormal) {
                return UserErrorCode.ERROR_USER_DISABLED
            }
            return CommonErrorCode.SUCCESS
        } catch (e: UserViewException) {
            return e.errorCode
        }
    }

    private fun tryGetUserId(credential: ContentAccessCredential): Long {
        val data = credential.rawData ?: throw IllegalArgumentException("Credential data should not be null")
        return when (data) {
            is Long -> data
            is UserTrait -> data.userId
            else -> throw IllegalArgumentException("Invalid credential data type: " + data.javaClass.name)
        }
    }
}
