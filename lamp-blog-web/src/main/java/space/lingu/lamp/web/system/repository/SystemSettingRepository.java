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

package space.lingu.lamp.web.system.repository;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.SystemSettingDao;
import space.lingu.lamp.web.database.repo.BaseRepository;
import space.lingu.lamp.web.system.setting.SystemSetting;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

/**
 * @author RollW
 */
@Repository
public class SystemSettingRepository extends BaseRepository<SystemSetting, String> {
    private final SystemSettingDao systemSettingDao;

    public SystemSettingRepository(LampDatabase lampDatabase,
                                   ContextThreadAware<PageableContext> pageableContextThreadAware,
                                   CacheManager cacheManager) {
        super(lampDatabase.getSystemSettingDao(), pageableContextThreadAware, cacheManager);
        this.systemSettingDao = lampDatabase.getSystemSettingDao();
    }

    @Override
    protected Class<SystemSetting> getEntityClass() {
        return SystemSetting.class;
    }
}