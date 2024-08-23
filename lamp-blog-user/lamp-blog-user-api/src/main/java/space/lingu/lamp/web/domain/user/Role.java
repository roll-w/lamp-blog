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

package space.lingu.lamp.web.domain.user;

import org.springframework.security.core.GrantedAuthority;
import space.lingu.lamp.web.domain.authorization.Authorities;

import java.util.List;

/**
 * For authorization, we use the concept of role.
 *
 * @author RollW
 */
public enum Role {
    USER(Authorities.USER),
    ADMIN(Authorities.ADMIN, Authorities.USER),
    STAFF(Authorities.STAFF, Authorities.USER),
    REVIEWER(Authorities.REVIEWER, Authorities.STAFF, Authorities.USER),
    CUSTOMER_SERVICE(Authorities.CUSTOMER_SERVICE, Authorities.STAFF, Authorities.USER),
    EDITOR(Authorities.EDITOR, Authorities.STAFF, Authorities.USER),
    ;

    private final List<GrantedAuthority> authority;

    Role(GrantedAuthority... authority) {
        this.authority = List.of(authority);
    }

    public boolean hasPrivilege() {
        return this == ADMIN;
    }

    public List<GrantedAuthority> toAuthority() {
        return authority;
    }

    public boolean hasAuthority(GrantedAuthority authority) {
        return this.authority.contains(authority);
    }
}
