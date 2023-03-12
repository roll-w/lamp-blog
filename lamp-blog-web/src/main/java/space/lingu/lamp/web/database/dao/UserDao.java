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

package space.lingu.lamp.web.database.dao;

import space.lingu.Dangerous;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.dto.UserInfo;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.Query;
import space.lingu.light.Transaction;
import space.lingu.light.Update;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class UserDao {
    @Insert
    public abstract long insert(User user);

    @Update
    public abstract void update(User... users);

    @Update
    public abstract void update(List<User> users);

    @Transaction
    @Delete("UPDATE user SET enabled = {enabled} WHERE id = {userId}")
    public abstract void updateEnabledByUser(long userId, boolean enabled);

    @Transaction
    @Delete("UPDATE user SET enabled = 1 WHERE id = {user.getId()}")
    protected abstract void updateEnabledByUser(User user);

    @Query("SELECT * FROM user WHERE id = {id}")
    public abstract User selectById(long id);

    @Delete("DELETE FROM user WHERE id = {id}")
    @Dangerous(message = "Will complete delete the user from database. This operation is dangerous.")
    protected abstract void deleteById(long id);

    // TODO: replace SELECT * with SELECT columns
    @Query("SELECT * FROM user")
    public abstract List<User> getAll();

    @Query("SELECT * FROM user ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    public abstract List<User> get(int offset, int limit);

    @Query("SELECT * FROM user WHERE enabled = {enabled} ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    public abstract List<User> getWithEnableState(int offset, int limit, boolean enabled);

    @Query("SELECT * FROM user WHERE account_canceled = {canceled} ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    public abstract List<User> getWithCanceledState(int offset, int limit, boolean canceled);

    @Query("SELECT * FROM user WHERE enabled = {enabled} AND account_canceled = {canceled} ORDER BY id ASC LIMIT {limit} OFFSET {offset}")
    public abstract List<User> getWithEnableCanceledState(int offset, int limit,  boolean enabled, boolean canceled);

    @Query("SELECT * FROM user WHERE id = {id}")
    public abstract User getUserById(long id);

    @Query("SELECT id, username, email, role FROM user WHERE id = {id}")
    public abstract UserInfo getUserInfoById(long id);

    @Query("SELECT * FROM user WHERE email = {email}")
    public abstract User getUserByEmail(String email);

    @Query("SELECT id FROM user WHERE email = {email}")
    public abstract Long getUserIdByEmail(String email);

    @Query("SELECT * FROM user WHERE username = {name}")
    public abstract User getUserByName(String name);

    @Query("SELECT id FROM user WHERE username = {name}")
    public abstract Long getUserIdByName(String name);

    @Query("SELECT 1 FROM user")
    public abstract Integer hasUsers();

    @Query("SELECT * FROM user WHERE id IN ({ids})")
    public abstract List<User> getUsersByIds(List<Long> ids);

    @Query("SELECT COUNT(*) FROM user")
    public abstract long getCount();

    @Query("SELECT COUNT(*) FROM user WHERE enabled = {enabled}")
    public abstract long getCountWithEnableState(boolean enabled);

    @Query("SELECT COUNT(*) FROM user WHERE canceled = {canceled}")
    public abstract long getCountWithCanceledState(boolean canceled);
}