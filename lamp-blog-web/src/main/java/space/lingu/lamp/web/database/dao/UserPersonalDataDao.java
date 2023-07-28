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
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface UserPersonalDataDao extends AutoPrimaryBaseDao<UserPersonalData> {
    @Override
    @Query("SELECT * FROM user_personal_data WHERE id = {id}")
    UserPersonalData getById(long id);

    @Override
    @Query("SELECT * FROM user_personal_data WHERE id IN ({ids})")
    List<UserPersonalData> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM user_personal_data ORDER BY id DESC")
    List<UserPersonalData> get();

    @Override
    @Query("SELECT COUNT(*) FROM user_personal_data")
    int count();

    @Override
    @Query("SELECT * FROM user_personal_data ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<UserPersonalData> get(Offset offset);

    @Override
    default String getTableName() {
        return "user_personal_data";
    }
}
