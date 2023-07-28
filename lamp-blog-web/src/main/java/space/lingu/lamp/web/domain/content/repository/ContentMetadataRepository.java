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

package space.lingu.lamp.web.domain.content.repository;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.ContentMetadataDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.content.ContentIdentity;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class ContentMetadataRepository extends AutoPrimaryBaseRepository<ContentMetadata> {
    private final ContentMetadataDao contentMetadataDao;

    public ContentMetadataRepository(LampDatabase lampDatabase,
                                     ContextThreadAware<PageableContext> pageableContextThreadAware,
                                     CacheManager cacheManager) {
        super(lampDatabase.getContentMetadataDao(), pageableContextThreadAware, cacheManager);
        this.contentMetadataDao = lampDatabase.getContentMetadataDao();
    }

    @Override
    protected Class<ContentMetadata> getEntityClass() {
        return ContentMetadata.class;
    }

    public ContentMetadata getById(long contentId, ContentType contentType) {
        return cacheResult(
                contentMetadataDao.getById(contentId, contentType)
        );
    }

    public List<ContentStatus> getStatusByIds(List<Long> contentIds,
                                              ContentType contentType) {
        return contentMetadataDao.getStatusByIds(contentIds, contentType);
    }

    public void updateStatus(ContentMetadata contentMetadata,
                             ContentStatus status) {

        contentMetadataDao.updateStatus(
                contentMetadata.getContentId(),
                contentMetadata.getContentType(),
                status
        );
        cacheResult(contentMetadata.toBuilder()
                .setContentStatus(status)
                .build()
        );
    }

    public List<ContentMetadata> getMetadataByIdentities(List<? extends ContentIdentity> contentIdentities) {
        return contentMetadataDao.getByIdentities(contentIdentities);
    }
}
