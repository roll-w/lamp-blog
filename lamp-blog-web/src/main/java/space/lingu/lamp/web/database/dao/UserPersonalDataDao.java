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

import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.light.Dao;
import space.lingu.light.DaoConnectionGetter;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;
import space.lingu.light.Update;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class UserPersonalDataDao implements DaoConnectionGetter {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(UserPersonalData... userPersonalData);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(List<UserPersonalData> userPersonalData);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(UserPersonalData... userPersonalData);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(List<UserPersonalData> userPersonalData);

    @Delete
    protected abstract void delete(UserPersonalData UserPersonalData);

    @Delete
    protected abstract void delete(List<UserPersonalData> userPersonalData);

    @Delete("DELETE FROM user_personal_data")
    protected abstract void clearTable();

    @Query("SELECT * FROM user_personal_data")
    public abstract List<UserPersonalData> get();

    @Query("SELECT * FROM user_personal_data WHERE id = {userId}")
    public abstract UserPersonalData getById(long userId);

    @Query("SELECT * FROM user_personal_data WHERE id IN ({ids})")
    public abstract List<UserPersonalData> getInIds(Long[] ids);
}
