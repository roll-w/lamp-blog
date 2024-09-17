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

package space.lingu.lamp.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import space.lingu.lamp.user.UserDetailsService;
import space.lingu.lamp.user.repository.UserDo;
import space.lingu.lamp.user.repository.UserRepository;

/**
 * @author RollW
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDo user = userRepository.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Username " + username + " not exist");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUserId(long userId) throws UsernameNotFoundException {
        UserDo user = userRepository.getByUserId(userId);
        if (user == null) {
            throw new UsernameNotFoundException("User ID " + userId + " not exist");
        }
        return user;
    }
}
