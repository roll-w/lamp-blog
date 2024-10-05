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

package space.lingu.lamp.security.authorization

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.ArrayDeque
import kotlin.collections.arrayListOf

/**
 * @author RollW
 */
object AuthorityHelper {
    @JvmStatic
    fun AuthorizationScope.getAuthorities(): List<GrantedAuthority> {
        val scopes = arrayListOf<AuthorizationScope>(this)
        val scopesToCheck = ArrayDeque<AuthorizationScope>()

        while (scopesToCheck.isNotEmpty()) {
            val scope = scopesToCheck.pop()
            if (scopes.contains(scope)) {
                continue
            }
            scopes.add(scope)
            scopesToCheck.addAll(scope.parents)
        }

        return scopes.map { SimpleGrantedAuthority(it.scope) }
    }

    @JvmStatic
    fun Collection<AuthorizationScope>.toAuthorities(): List<GrantedAuthority> {
        return flatMap { it.getAuthorities() }
    }

    @JvmStatic
    fun Privilege.toAuthorities(): List<GrantedAuthority> {
        return scopes.toAuthorities()
    }

    @JvmStatic
    fun Collection<AuthorizationScope>.distinctByScope(): List<AuthorizationScope> {
        return distinctBy { it.scope }
    }

    @JvmStatic
    fun Collection<AuthorizationScope>.toPrivilege(): Privilege {
        return SimplePrivilege(this.toList())
    }
}