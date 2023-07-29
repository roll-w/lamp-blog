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

import space.lingu.lamp.web.system.setting.SystemSetting;
import space.lingu.light.Dao;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface SystemSettingDao extends LampDao<SystemSetting, String> {
    @Override
    @Query("SELECT `key`, `value` FROM system_setting WHERE `key` = {id}")
    SystemSetting getById(String id);

    @Override
    @Query("SELECT `key`, `value` FROM system_setting WHERE `key` IN ({ids})")
    List<SystemSetting> getByIds(List<String> ids);

    @Override
    @Query("SELECT `key`, `value` FROM system_setting ORDER BY id DESC")
    List<SystemSetting> get();

    @Override
    @Query("SELECT COUNT(*) FROM system_setting")
    int count();

    @Override
    @Query("SELECT `key`, `value` FROM system_setting ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<SystemSetting> get(Offset offset);

    @Override
    default String getTableName() {
        return "system_setting";
    }

    @Query("SELECT `key`, `value` FROM system_setting WHERE `key` = {key}")
    SystemSetting getByKey(String key);
}
