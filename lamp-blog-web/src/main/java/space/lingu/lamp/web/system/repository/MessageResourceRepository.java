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
import space.lingu.lamp.web.database.dao.MessageResourceDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.system.MessageResource;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.List;
import java.util.Locale;

/**
 * @author RollW
 */
@Repository
public class MessageResourceRepository extends AutoPrimaryBaseRepository<MessageResource> {
    private final MessageResourceDao messageResourceDao;

    public MessageResourceRepository(LampDatabase lampDatabase,
                                     ContextThreadAware<PageableContext> pageableContextThreadAware,
                                     CacheManager cacheManager) {
        super(lampDatabase.getMessageResourceDao(), pageableContextThreadAware, cacheManager);
        this.messageResourceDao = lampDatabase.getMessageResourceDao();
    }

    @Override
    protected Class<MessageResource> getEntityClass() {
        return MessageResource.class;
    }

    public List<MessageResource> getByKey(String key) {
        return cacheResult(
                messageResourceDao.getByKey(key)
        );
    }

    public MessageResource getByKeyAndLocale(String key, Locale locale) {
        return cacheResult(
                messageResourceDao.getByKeyAndLocale(key, locale)
        );
    }
}
