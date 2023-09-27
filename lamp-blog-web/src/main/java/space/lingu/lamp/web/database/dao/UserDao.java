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

import space.lingu.lamp.web.domain.user.User;
import space.lingu.light.Dao;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface UserDao extends AutoPrimaryBaseDao<User> {
    @Override
    @Query("SELECT * FROM user WHERE id = {id}")
    User getById(long id);

    @Override
    @Query("SELECT * FROM user WHERE id IN ({ids})")
    List<User> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM user ORDER BY id DESC")
    List<User> get();

    @Override
    @Query("SELECT COUNT(*) FROM user")
    int count();

    @Override
    @Query("SELECT * FROM user ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<User> get(Offset offset);

    @Override
    default String getTableName() {
        return "user";
    }

    @Query("SELECT * FROM user WHERE email = {email}")
    User getByEmail(String email);

    @Query("SELECT id FROM user WHERE email = {email}")
    Long getIdByEmail(String email);

    @Query("SELECT * FROM user WHERE username = {name}")
    User getByUsername(String name);

    @Query("SELECT id FROM user WHERE username = {name}")
    Long getIdByUsername(String name);

    @Query("SELECT 1 FROM user")
    Integer hasUsers();


    @Query("SELECT * FROM user WHERE username = {name}")
    User getUserByName(String name);


    @Query("SELECT * FROM user WHERE username LIKE CONCAT('%', {username}, '%')")
    List<User> getUsersLikeUsername(String username);

}
