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

package space.lingu.lamp.web.domain.authorization;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Predefined authorities.
 *
 * @author RollW
 */
public final class Authorities {
    public static final GrantedAuthority USER = new SimpleGrantedAuthority("USER");

    public static final GrantedAuthority ADMIN = new SimpleGrantedAuthority("ADMIN");

    public static final GrantedAuthority STAFF = new SimpleGrantedAuthority("STAFF");

    public static final GrantedAuthority REVIEWER = new SimpleGrantedAuthority("REVIEWER");

    public static final GrantedAuthority CUSTOMER_SERVICE = new SimpleGrantedAuthority("CUSTOMER_SERVICE");

    public static final GrantedAuthority EDITOR = new SimpleGrantedAuthority("EDITOR");

    public static final GrantedAuthority[] ALL = new GrantedAuthority[]{
            USER,
            ADMIN,
            STAFF,
            REVIEWER,
            CUSTOMER_SERVICE,
            EDITOR,
    };

    private Authorities() {
    }
}
