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

package tech.lamprism.lampray.security.authorization

import tech.lamprism.lampray.security.authorization.RoleBasedPrivilege.Companion.toPrivilege
import tech.lamprism.lampray.user.AttributedUserDetails

/**
 * @author RollW
 */
class PrivilegeAttributedUser(
    private val attributedUser: AttributedUserDetails,
    private val privilege: Privilege = attributedUser.role.toPrivilege()
) : PrivilegedUser, AttributedUserDetails by attributedUser, Privilege by privilege {
    override val scopes: List<AuthorizationScope>
        get() = (attributedUser.role.toPrivilege() + privilege).scopes
}