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

package space.lingu.lamp.web.data.entity.user;

import space.lingu.light.Constructor;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.LightConfiguration;
import space.lingu.light.PrimaryKey;

import java.io.Serializable;

/**
 * @author RollW
 */
@DataTable(tableName = "user_table", indices = {
        @Index(value = "username", unique = true)
})
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "120")
public class User implements Serializable {
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

    @DataColumn(name = "email")
    private final String email;

    // private String phone;

    @DataColumn(name = "enabled")
    private final boolean enabled;

    @DataColumn(name = "locked")
    private final boolean locked;

    @DataColumn(name = "account_expired")
    private final boolean accountExpired;

    @Constructor
    public User(Long id, String username, String password,
                Role role, long registerTime, String email,
                boolean enabled, boolean locked, boolean accountExpired) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.registerTime = registerTime;
        this.email = email;
        this.enabled = enabled;
        this.locked = locked;
        this.accountExpired = accountExpired;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public long getRegisterTime() {
        return registerTime;
    }

    public String getEmail() {
        return email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder()
                .setId(id)
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                .setRegisterTime(registerTime)
                .setEmail(email)
                .setEnabled(enabled)
                .setLocked(locked)
                .setAccountExpired(accountExpired);
    }

    public static final class Builder {
        private Long id;
        private String username;
        private String password;
        private Role role;
        private long registerTime;
        private String email;
        private boolean enabled;
        private boolean locked;
        private boolean accountExpired;

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

        public Builder setEmail(String email) {
            this.email = email;
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

        public User build() {
            return new User(
                    id, username, password,
                    role, registerTime,
                    email, enabled, locked, accountExpired);
        }
    }
}
