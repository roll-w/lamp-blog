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

package tech.lamprism.lampray.security.authentication.adapter;

import com.google.common.base.Verify;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import tech.lamprism.lampray.user.UserTrait;

import java.util.List;

/**
 * @author RollW
 */
public class PreUserAuthenticationToken extends AbstractAuthenticationToken {
    private final UserTrait user;

    public PreUserAuthenticationToken(UserTrait user) {
        super(List.of());
        Verify.verifyNotNull(user, "user must not be null");
        this.user = user;
    }

    @Override
    public UserTrait getCredentials() {
        return user;
    }

    @Override
    public UserTrait getPrincipal() {
        return user;
    }
}
