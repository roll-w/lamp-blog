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

import tech.lamprism.lampray.user.Role

/**
 * Role-based authorization scope.
 *
 * Used for pre-defined authorization scopes associated
 * with [Role].
 *
 * @author RollW
 */
enum class RoleBasedAuthorizationScope(
    override val scope: String,
    val role: Role,
    override val parent: AuthorizationScope? = null,
) : AuthorizationScope, LineAuthorizationScope,
    AuthorizationScopeSupplier {
    USER("role:USER", Role.USER),
    STAFF("role:STAFF", Role.STAFF, USER),
    ADMIN("role:ADMIN", Role.ADMIN, USER),
    REVIEWER("role:REVIEWER", Role.REVIEWER, STAFF),
    CUSTOMER_SERVICE("role:CUSTOMER_SERVICE", Role.CUSTOMER_SERVICE, STAFF),
    EDITOR("role:EDITOR", Role.EDITOR, STAFF);

    override val authorizationScopes: List<AuthorizationScope>
        get() = VALUES

    companion object {
        fun Role.toScope() = values()
            .first {
                it.role == this
            }

        @JvmStatic
        private val VALUES = values().toList()
    }
}
