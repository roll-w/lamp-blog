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

package space.lingu.lamp.web.domain.userdetails.repository;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.UserPersonalDataDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

/**
 * @author RollW
 */
@Repository
public class UserPersonalDataRepository extends AutoPrimaryBaseRepository<UserPersonalData> {
    private final UserPersonalDataDao userPersonalDataDao;

    public UserPersonalDataRepository(LampDatabase lampDatabase,
                                      ContextThreadAware<PageableContext> pageableContextThreadAware,
                                      CacheManager cacheManager) {
        super(lampDatabase.getUserPersonalDataDao(), pageableContextThreadAware, cacheManager);
        this.userPersonalDataDao = lampDatabase.getUserPersonalDataDao();
    }

    @Override
    protected Class<UserPersonalData> getEntityClass() {
        return UserPersonalData.class;
    }
}