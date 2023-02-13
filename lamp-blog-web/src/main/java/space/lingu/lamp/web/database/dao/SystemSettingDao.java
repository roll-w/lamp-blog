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

import space.lingu.lamp.web.system.SystemSetting;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class SystemSettingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(SystemSetting... systemSettings);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<SystemSetting> systemSettings);

    @Delete
    public abstract void delete(SystemSetting systemSetting);

    @Delete("DELETE FROM system_setting WHERE `key` = {key}")
    public abstract void deleteByKey(String key);

    @Query("SELECT `key`, `value` FROM system_setting WHERE `key` = {key}")
    public abstract SystemSetting getByKey(String key);

    @Query("SELECT `key`, `value` FROM system_setting")
    public abstract List<SystemSetting> get();

    @Query("SELECT `key`, `value` FROM system_setting ORDER BY `key` LIMIT {limit} OFFSET {offset}")
    public abstract List<SystemSetting> get(int limit, int offset);
}
