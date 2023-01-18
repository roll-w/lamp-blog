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

package space.lingu.lamp.web.domain.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.user.repository.UserRepository;
import space.lingu.lamp.web.domain.user.User;

/**
 * @author RollW
 */
@Service
public class UserServiceImpl implements UserDetailsService, UserSignatureProvider {
    private final UserRepository userRepository;
    // TODO: user cache

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not exist");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUserId(long userId) throws UsernameNotFoundException {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new UsernameNotFoundException("UserID " + userId + " not exist");
        }
        return user;
    }

    @Override
    public String getSignature(long userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            return null;
        }
        return "LampBlogUserSignature-" + user.getPassword();
    }
}
