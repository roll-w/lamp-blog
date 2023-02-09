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

import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.lamp.web.domain.user.common.UserViewException;

import java.util.List;

/**
 * Provides to user APIs, which are used to get user information.
 * Admin APIs should use {@link UserManageService}.
 *
 * @author RollW
 */
public interface UserSearchService {
    /**
     * Get user by id. And enables check if user is deleted or canceled.
     *
     * @throws UserViewException if user is deleted or canceled.
     */
    UserIdentity findUser(long userId) throws UserViewException;

    /**
     * Get user by id. And enables check if user is deleted or canceled.
     *
     * @throws UserViewException if user is deleted or canceled.
     */
    UserIdentity findUser(UserIdentity userIdentity) throws UserViewException;

    List<? extends UserIdentity> findUsers(int page, int size);

    List<? extends UserIdentity> findUsers();
}
