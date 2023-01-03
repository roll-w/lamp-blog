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

import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.data.database.LampDatabase;
import space.lingu.lamp.web.data.database.dao.SystemSettingDao;
import space.lingu.lamp.web.data.entity.SystemSetting;

/**
 * @author RollW
 */
@Repository
public class SystemSettingRepository {
    private final SystemSettingDao systemSettingDao;

    public SystemSettingRepository(LampDatabase database) {
        this.systemSettingDao = database.getSystemSettingDao();
    }

    public String get(String key) {
        SystemSetting setting = systemSettingDao.getByKey(key);
        if (setting == null) {
            return null;
        }
        return setting.getKey();
    }

    public SystemSetting getSystemSetting(String key) {
       return systemSettingDao.getByKey(key);
    }

    public void set(String key, String value) {
        SystemSetting setting = new SystemSetting(key, value);
        systemSettingDao.insert(setting);
    }

    public void set(SystemSetting systemSetting) {
        systemSettingDao.insert(systemSetting);
    }

    public void delete(String key) {
        systemSettingDao.deleteByKey(key);
    }
}
