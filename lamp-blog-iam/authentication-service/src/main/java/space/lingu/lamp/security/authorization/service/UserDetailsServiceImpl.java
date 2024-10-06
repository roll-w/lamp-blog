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

package space.lingu.lamp.security.authorization.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.lingu.lamp.security.authorization.RoleBasedPrivilege;
import space.lingu.lamp.security.authorization.adapter.userdetails.ScopeBasedUserDetails;
import space.lingu.lamp.security.authorization.adapter.userdetails.UserDetailsService;
import space.lingu.lamp.user.AttributedUserDetails;
import space.lingu.lamp.user.UserProvider;
import space.lingu.lamp.user.UserViewException;

/**
 * @author RollW
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserProvider userProvider;

    public UserDetailsServiceImpl(UserProvider userProvider) {
        this.userProvider = userProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            AttributedUserDetails user = userProvider.getUser(username);
            return new ScopeBasedUserDetails(user, new RoleBasedPrivilege(user.getRole()));
        } catch (UserViewException e) {
            throw new UsernameNotFoundException("Username " + username + " not exist");
        }
    }

    @Override
    public UserDetails loadUserByUserId(long userId) throws UsernameNotFoundException {
        try {
            AttributedUserDetails user = userProvider.getUser(userId);
            return new ScopeBasedUserDetails(user, new RoleBasedPrivilege(user.getRole()));
        } catch (UserViewException e) {
            throw new UsernameNotFoundException("User ID " + userId + " not exist");
        }
    }
}
