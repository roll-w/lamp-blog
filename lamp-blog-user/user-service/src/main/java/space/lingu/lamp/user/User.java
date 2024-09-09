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

package space.lingu.lamp.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import space.lingu.NonNull;
import space.lingu.lamp.DataEntity;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.light.Constructor;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.LightConfiguration;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.Collection;
import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(name = "space/lingu/lamp/user", indices = {
        @Index(value = "username", unique = true)
})
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120")
@SuppressWarnings({"ClassCanBeRecord"})
public class User implements UserDetails, AttributedUserDetails, DataEntity<Long> {
    @PrimaryKey(autoGenerate = true)
    @DataColumn(name = "id")
    private final Long id;

    @DataColumn(name = "username", nullable = false)
    private final String username;

    @DataColumn(name = "password", nullable = false)
    private final String password;

    @DataColumn(name = "role")
    @LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "20")
    private final Role role;

    @DataColumn(name = "register_time")
    private final long registerTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    @DataColumn(name = "email")
    private final String email;

    @DataColumn(name = "phone")
    private final String phone;

    @DataColumn(name = "enabled")
    private final boolean enabled;

    @DataColumn(name = "locked")
    private final boolean locked;

    @DataColumn(name = "account_expired")
    private final boolean accountExpired;

    @DataColumn(name = "account_canceled")
    private final boolean canceled;

    @Constructor
    public User(Long id, String username, String password,
                Role role, long registerTime,
                long updateTime, String email, String phone,
                boolean enabled, boolean locked,
                boolean accountExpired,
                boolean canceled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.registerTime = registerTime;
        this.updateTime = updateTime;
        this.email = email;
        this.phone = phone;
        this.enabled = enabled;
        this.locked = locked;
        this.accountExpired = accountExpired;
        this.canceled = canceled;
    }

    public Long getId() {
        return id;
    }

    @Override
    public Long getResourceId() {
        return getId();
    }

    @Override
    public long getCreateTime() {
        return getRegisterTime();
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    @Override
    public long getUserId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Role getRole() {
        return role;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public boolean isCanceled() {
        return canceled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired || !canceled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.toAuthority();
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return registerTime == user.registerTime && enabled == user.enabled && locked == user.locked && accountExpired == user.accountExpired && canceled == user.canceled && Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && role == user.role && Objects.equals(email, user.email) && Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, role, registerTime, email, phone, enabled, locked, accountExpired, canceled);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", registerTime=" + registerTime +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", accountExpired=" + accountExpired +
                ", canceled=" + canceled +
                '}';
    }

    public static boolean isInvalidId(Long userId) {
        if (userId == null) {
            return true;
        }
        return userId <= 0;
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return UserResourceKind.INSTANCE;
    }

    public static final class Builder implements LongEntityBuilder<User> {
        private Long id = null;
        private String username;
        private String password;
        private Role role = Role.USER;
        private long registerTime;
        private long updateTime;
        private String email;
        private String phone;
        private boolean enabled;
        private boolean locked = false;
        private boolean accountExpired = false;
        private boolean canceled = false;

        public Builder() {

        }

        public Builder(User user) {
            this.id = user.id;
            this.username = user.username;
            this.password = user.password;
            this.role = user.role;
            this.registerTime = user.registerTime;
            this.email = user.email;
            this.phone = user.phone;
            this.enabled = user.enabled;
            this.locked = user.locked;
            this.accountExpired = user.accountExpired;
            this.canceled = user.canceled;
        }

        @Override
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setRole(Role role) {
            this.role = role;
            return this;
        }

        public Builder setRegisterTime(long registerTime) {
            this.registerTime = registerTime;
            return this;
        }

        public Builder setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder setLocked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Builder setAccountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public Builder setCanceled(boolean canceled) {
            this.canceled = canceled;
            return this;
        }

        @Override
        public User build() {
            return new User(
                    id, username, password,
                    role, registerTime,
                    updateTime, email, phone, enabled, locked,
                    accountExpired, canceled
            );
        }
    }
}
