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

package space.lingu.lamp.security.authorization.adapter.userdetails

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import space.lingu.lamp.security.authorization.AuthorityHelper.toAuthorities
import space.lingu.lamp.security.authorization.Privilege
import space.lingu.lamp.security.authorization.RoleBasedPrivilege.Companion.toPrivilege
import space.lingu.lamp.user.AttributedUserDetails

/**
 * @author RollW
 */
class ScopeBasedUserDetails(
    private val attributedUser: AttributedUserDetails,
    private val privilege: Privilege
) : UserDetails, AttributedUserDetails by attributedUser {
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val roleBasedPrivilege = attributedUser.role.toPrivilege()
        return (privilege + roleBasedPrivilege).toAuthorities()
    }

    override fun getPassword(): String {
        return attributedUser.password
    }

    override fun getUsername(): String {
        return attributedUser.username
    }

    override fun isAccountNonExpired(): Boolean {
        return attributedUser.isCanceled().not()
    }

    override fun isAccountNonLocked(): Boolean {
        return attributedUser.isLocked.not()
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return attributedUser.isEnabled()
    }
}