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

import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.ContentMetadataDao;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;

/**
 * @author RollW
 */
@Repository
public class ContentMetadataRepository {
    private final ContentMetadataDao contentMetadataDao;

    public ContentMetadataRepository(LampDatabase database) {
        this.contentMetadataDao = database.getContentMetadataDao();
    }

    public void insert(ContentMetadata contentMetadata) {
        contentMetadataDao.insert(contentMetadata);
    }

    public void update(ContentMetadata contentMetadata) {
        contentMetadataDao.update(contentMetadata);
    }

    public ContentMetadata getById(String contentId, ContentType contentType) {
        return contentMetadataDao.getById(contentId, contentType);
    }

    public void updateStatus(String contentId, ContentType contentType, ContentStatus status) {
        contentMetadataDao.updateStatus(contentId, contentType, status);
    }
}
