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

package space.lingu.lamp.security.authentication.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import space.lingu.lamp.security.authorization.PrivilegedUser;

/**
 * Provide a base class for authentication providers based
 * on privileged users.
 *
 * @author RollW
 */
public abstract class PrivilegedUserBasedAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LoggerFactory.getLogger(PrivilegedUserBasedAuthenticationProvider.class);

    protected Logger getLogger() {
        return logger;
    }

    protected void check(PrivilegedUser user) throws AuthenticationException {
        if (user.isLocked()) {
            getLogger().debug("Failed to authenticate since user account is locked, user[{}]", user.getUserId());
            throw new LockedException("User account is locked");
        }
        if (!user.isEnabled()) {
            getLogger().debug("Failed to authenticate since user account is disabled, user[{}]", user.getUserId());
            throw new DisabledException("User account is disabled");
        }
        if (user.isCanceled()) {
            getLogger().debug("Failed to authenticate since user account has canceled, user[{}]", user.getUserId());
            throw new CanceledException("User account has canceled");
        }
    }
}
