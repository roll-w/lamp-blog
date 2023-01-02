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

package space.lingu.lamp.web.data.database.repository;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.data.database.LampDatabase;
import space.lingu.lamp.web.data.database.dao.UserDao;
import space.lingu.lamp.web.data.entity.user.User;

import java.util.concurrent.CompletableFuture;

/**
 * @author RollW
 */
@Repository
public class UserRepository {
    private final UserDao userDao;

    public UserRepository(LampDatabase database) {
        this.userDao = database.getUserDao();
    }

    public long insertUser(User user) {
        return userDao.insert(user);
    }

    @Async
    public CompletableFuture<Long> asyncInsertUser(User user) {
        return CompletableFuture.completedFuture(
                user.getId()
        );
    }

    public User getUserById(long id) {
        return userDao.getUserById(id);
    }

    public User getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    public Long getUserIdByName(String name) {
        return userDao.getUserIdByName(name);
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    public Long getUserIdByEmail(String email) {
        return userDao.getUserIdByEmail(email);
    }

    public boolean isExistByEmail(String email) {
        return !User.isInvalidId(getUserIdByEmail(email));
    }

    public boolean hasUsers() {
        return userDao.hasUsers() != null;
    }
}
