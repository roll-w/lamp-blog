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

import space.lingu.lamp.authentication.register.RegisterVerificationToken;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Query;
import space.lingu.light.Transaction;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface RegisterVerificationTokenDao extends AutoPrimaryBaseDao<RegisterVerificationToken> {
    @Override
    @Query("SELECT * FROM register_verification_token WHERE id = {id}")
    RegisterVerificationToken getById(long id);

    @Override
    @Query("SELECT * FROM register_verification_token WHERE id IN ({ids})")
    List<RegisterVerificationToken> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM register_verification_token ORDER BY id DESC")
    List<RegisterVerificationToken> get();

    @Override
    @Query("SELECT COUNT(*) FROM register_verification_token")
    int count();

    @Override
    @Query("SELECT * FROM register_verification_token ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<RegisterVerificationToken> get(Offset offset);

    @Override
    default String getTableName() {
        return "register_verification_token";
    }

    @Transaction
    @Delete("UPDATE register_verification_token SET used = {used} WHERE token = {token}")
    void updateUsedByToken(String token, boolean used);

    @Query("SELECT * FROM register_verification_token WHERE token = {token}")
    RegisterVerificationToken findByToken(String token);

    @Query("SELECT * FROM register_verification_token WHERE user_id = {userId} LIMIT 1")
    RegisterVerificationToken findByUserId(long userId);
}
