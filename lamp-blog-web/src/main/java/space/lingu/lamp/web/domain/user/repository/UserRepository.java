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
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.UserDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.user.User;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author RollW
 */
@Repository
public class UserRepository extends AutoPrimaryBaseRepository<User> {
    private final UserDao userDao;

    public UserRepository(LampDatabase lampDatabase,
                          ContextThreadAware<PageableContext> pageableContextThreadAware,
                          CacheManager cacheManager) {
        super(lampDatabase.getUserDao(), pageableContextThreadAware, cacheManager);
        this.userDao = lampDatabase.getUserDao();
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    protected void onInvalidatedCache(User user) {
        cache.evictIfPresent(user.getUsername());
        cache.evictIfPresent(user.getEmail());
    }

    @Override
    protected void onCache(User user) {
        hasUsers.set(true);
        cache.put(user.getUsername(), user);
        cache.put(user.getEmail(), user);
    }

    public User getByEmail(String email) {
        Cache.ValueWrapper wrapper =
                cache.get(email);
        if (wrapper == null) {
            return cacheResult(
                    userDao.getByEmail(email)
            );
        }
        return (User) wrapper.get();
    }

    public User getByUsername(String username) {
        Cache.ValueWrapper wrapper =
                cache.get(username);
        if (wrapper == null) {
            return cacheResult(
                    userDao.getByUsername(username)
            );
        }
        return (User) wrapper.get();
    }

    public void enableUser(User user) {
        User updated = user.toBuilder()
                .setEnabled(true)
                .setUpdateTime(System.currentTimeMillis())
                .build();
        update(updated);
    }


    public boolean isExistByEmail(String email) {
        User cached = cache.get(email, User.class);
        if (cached != null) {
            return true;
        }
        return getByEmail(email) != null;
    }

    private final AtomicBoolean hasUsers = new AtomicBoolean(false);

    public boolean hasUsers() {
        if (hasUsers.get()) {
            return true;
        }
        Integer has = userDao.hasUsers();
        if (has != null) {
            hasUsers.set(has > 0);
        }
        return hasUsers.get();
    }
}
