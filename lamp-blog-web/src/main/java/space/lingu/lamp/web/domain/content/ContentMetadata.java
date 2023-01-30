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

package space.lingu.lamp.web.domain.content;

import space.lingu.light.Constructor;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(tableName = "content_metadata", indices =
@Index(value = {"content_id", "type"}, unique = true))
@SuppressWarnings({"ClassCanBeRecord", "unused"})
public class ContentMetadata {
    // only maintains the metadata of the content.
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "content_id")
    private final String contentId;

    @DataColumn(name = "type")
    private final ContentType contentType;

    @DataColumn(name = "status")
    private final ContentStatus contentStatus;

    @Constructor
    public ContentMetadata(Long id, long userId,
                           String contentId, ContentType contentType,
                           ContentStatus contentStatus) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.contentStatus = contentStatus;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public static class Builder {
        private Long id;
        private long userId;
        private String contentId;
        private ContentType contentType;
        private ContentStatus contentStatus;

        public Builder() {
        }

        public Builder(ContentMetadata contentMetadata) {
            this.id = contentMetadata.id;
            this.userId = contentMetadata.userId;
            this.contentId = contentMetadata.contentId;
            this.contentType = contentMetadata.contentType;
            this.contentStatus = contentMetadata.contentStatus;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setContentId(String contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder setContentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setContentStatus(ContentStatus contentStatus) {
            this.contentStatus = contentStatus;
            return this;
        }

        public ContentMetadata build() {
            return new ContentMetadata(id, userId, contentId, contentType, contentStatus);
        }
    }
}
