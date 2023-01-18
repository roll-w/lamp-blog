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

package space.lingu.lamp.web.domain.user.repository;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.common.CacheNames;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.UserDao;
import space.lingu.lamp.web.domain.user.User;

/**
 * @author RollW
 */
@Repository
public class UserRepository {
    private final UserDao userDao;
    private final Cache userCache;

    public UserRepository(LampDatabase database,
                          CacheManager cacheManager) {
        this.userDao = database.getUserDao();
        this.userCache = cacheManager.getCache(CacheNames.USERS);
    }

    public long insertUser(User user) {
        if (user == null) {
            return -1;
        }
        long id = userDao.insert(user);
        User newUser = user
                .toBuilder()
                .setId(id)
                .build();
        updateCache(newUser);
        return id;
    }

    public void update(User user) {
        userDao.update(user);
        updateCache(user);
    }

    @Async
    public void makeUserEnabled(User user) {
        userDao.updateEnabledByUser(user.getId(), true);
        User newUser = user.toBuilder()
                .setEnabled(true)
                .build();
        updateCache(newUser);
    }

    @Async
    public void asyncInsertUser(User user) {
        userDao.insert(user);
        updateCache(user);
    }

    public User getUserById(long id) {
        User cached = userCache.get(id, User.class);
        if (cached != null) {
            return cached;
        }
        User queried = userDao.getUserById(id);
        updateCache(queried);
        return queried;
    }

    public User getUserByName(String name) {
        User cached = userCache.get(name, User.class);
        if (cached != null) {
            return cached;
        }
        User queried = userDao.getUserByName(name);
        updateCache(queried);
        return queried;
    }

    private Long getUserIdByName(String name) {
        return userDao.getUserIdByName(name);
    }

    public boolean isExistByName(String name) {
        return !User.isInvalidId(getUserIdByName(name));
    }

    public User getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    private Long getUserIdByEmail(String email) {
        return userDao.getUserIdByEmail(email);
    }

    public boolean isExistByEmail(String email) {
        return !User.isInvalidId(getUserIdByEmail(email));
    }

    public boolean hasUsers() {
        return userDao.hasUsers() != null;
    }

    private void updateCache(User user) {
        userCache.put(user.getId(), user);
        userCache.put(user.getUsername(), user);
        userCache.put(user.getEmail(), user);
    }

    private void removeCache(User user) {
        userCache.evictIfPresent(user.getId());
        userCache.evictIfPresent(user.getUsername());
        userCache.evictIfPresent(user.getEmail());
    }
}
