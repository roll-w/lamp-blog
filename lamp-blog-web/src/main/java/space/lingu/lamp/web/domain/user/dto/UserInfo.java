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

package space.lingu.lamp.web.domain.user.dto;

import org.springframework.security.core.userdetails.UserDetails;
import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.light.DataColumn;

/**
 * @author RollW
 */
public record UserInfo(
        @DataColumn(name = "id")
        long id,

        @DataColumn(name = "username")
        String username,

        @DataColumn(name = "email")
        String email,

        @DataColumn(name = "role")
        Role role
) {
    public static UserInfo from(User user) {
        if (user == null) {
            return null;
        }
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public static UserInfo from(UserDetails userDetails) {
        if (!(userDetails instanceof User user)) {
            return null;
        }
        return new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
