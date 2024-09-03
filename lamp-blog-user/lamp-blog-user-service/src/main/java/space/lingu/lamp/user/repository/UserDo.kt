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
package space.lingu.lamp.user.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import space.lingu.NonNull
import space.lingu.lamp.web.domain.AttributedUserDetails
import space.lingu.lamp.web.domain.user.Role
import space.lingu.lamp.web.domain.user.User
import space.lingu.lamp.web.domain.user.UserResourceKind
import tech.rollw.common.web.system.SystemResource
import tech.rollw.common.web.system.SystemResourceKind
import java.io.Serializable

/**
 * @author RollW
 */
@Entity
@Table(name = "user")
class UserDo : Serializable, SystemResource<Long>, UserDetails, AttributedUserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "username")
    private var username: String? = null

    @Column(name = "password")
    private var password: String? = null

    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    private var role: Role? = null

    @Column(name = "register_time")
    var registerTime: Long = 0

    @Column(name = "update_time")
    private var updateTime: Long = 0

    @Column(name = "email")
    private var email: String? = null

    @Column(name = "phone")
    var phone: String? = null

    @Column(name = "enabled")
    private var enabled = false

    @Column(name = "locked")
    private var locked = false

    @Column(name = "account_expired")
    var accountExpired: Boolean = false

    @Column(name = "account_canceled")
    private var canceled = false

    constructor(
        id: Long?, username: String?, password: String?,
        role: Role?, registerTime: Long,
        updateTime: Long, email: String?, phone: String?,
        enabled: Boolean, locked: Boolean,
        accountExpired: Boolean,
        canceled: Boolean
    ) {
        this.id = id
        this.username = username
        this.password = password
        this.role = role
        this.registerTime = registerTime
        this.updateTime = updateTime
        this.email = email
        this.phone = phone
        this.enabled = enabled
        this.locked = locked
        this.accountExpired = accountExpired
        this.canceled = canceled
    }

    constructor()

    override fun getResourceId(): Long {
        return id!!
    }

    @NonNull
    override fun getSystemResourceKind(): SystemResourceKind {
        return UserResourceKind
    }

    override fun getCreateTime(): Long {
        return registerTime
    }

    override fun getUpdateTime(): Long {
        return updateTime
    }

    fun setUpdateTime(updateTime: Long) {
        this.updateTime = updateTime
    }

    override fun getUserId(): Long {
        return id!!
    }

    override fun getEmail(): String {
        return email!!
    }

    fun setEmail(email: String?) {
        this.email = email
    }

    override fun getRole(): Role {
        return role!!
    }

    fun setRole(role: Role?) {
        this.role = role
    }

    override fun isLocked(): Boolean {
        return locked
    }

    fun setLocked(locked: Boolean) {
        this.locked = locked
    }

    override fun isCanceled(): Boolean {
        return canceled
    }

    fun setCanceled(canceled: Boolean) {
        this.canceled = canceled
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return role!!.toAuthority()
    }

    override fun getPassword(): String {
        return password!!
    }

    fun setPassword(password: String?) {
        this.password = password
    }

    override fun getUsername(): String {
        return username!!
    }

    fun setUsername(username: String?) {
        this.username = username
    }

    override fun isAccountNonExpired(): Boolean {
        return !accountExpired || !canceled
    }

    override fun isAccountNonLocked(): Boolean {
        return !locked
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    override fun toString(): String {
        return "UserDo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", registerTime=" + registerTime +
                ", updateTime=" + updateTime +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", accountExpired=" + accountExpired +
                ", canceled=" + canceled +
                '}'
    }

    fun toUser(): User {
        return User(
            id, username, password, role,
            registerTime, updateTime, email, phone,
            enabled, locked, accountExpired,
            canceled
        )
    }

    companion object {
        @JvmStatic
        fun User.toDo(): UserDo {
            return UserDo(
                id, username, password, role,
                registerTime, updateTime, email, phone,
                isEnabled, isLocked, isAccountExpired,
                isCanceled
            )
        }
    }
}
