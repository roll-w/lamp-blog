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

import space.lingu.NonNull;
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.*;
import tech.rollw.common.web.system.SystemResourceKind;

/**
 * @author RollW
 */
@DataTable(name = "content_metadata", indices = {
        @Index(value = {"content_id", "type"}, unique = true)
})
@SuppressWarnings({"ClassCanBeRecord"})
public class ContentMetadata implements LongDataItem<ContentMetadata>, ContentTrait {
    // only maintains the metadata of the content.
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "content_id", nullable = false)
    private final long contentId;

    @DataColumn(name = "type", nullable = false)
    @NonNull
    private final ContentType contentType;

    @DataColumn(name = "status", nullable = false)
    @NonNull
    private final ContentStatus contentStatus;

    @DataColumn(name = "auth_type", nullable = false)
    @NonNull
    private final ContentAccessAuthType contentAccessAuthType;

    @Constructor
    public ContentMetadata(Long id, long userId,
                           long contentId,
                           @NonNull ContentType contentType,
                           @NonNull ContentStatus contentStatus,
                           @NonNull ContentAccessAuthType contentAccessAuthType) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.contentStatus = contentStatus;
        this.contentAccessAuthType = contentAccessAuthType;
        checkForNull();
    }

    @SuppressWarnings("all")
    private void checkForNull() {
        if (contentType == null) {
            throw new NullPointerException("contentType is null");
        }
        if (contentStatus == null) {
            throw new NullPointerException("contentStatus is null");
        }
        if (contentAccessAuthType == null) {
            throw new NullPointerException("contentAccessAuthType is null");
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public long getUpdateTime() {
        return 0;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public long getContentId() {
        return contentId;
    }

    @NonNull
    @Override
    public ContentType getContentType() {
        return contentType;
    }

    @NonNull
    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    @NonNull
    public ContentAccessAuthType getContentAccessAuthType() {
        return contentAccessAuthType;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.CONTENT_METADATA;
    }

    public static class Builder implements LongEntityBuilder<ContentMetadata> {
        private Long id;
        private long userId;
        private long contentId;
        private ContentType contentType;
        private ContentStatus contentStatus;
        private ContentAccessAuthType contentAccessAuthType;

        public Builder() {
        }

        public Builder(ContentMetadata contentMetadata) {
            this.id = contentMetadata.id;
            this.userId = contentMetadata.userId;
            this.contentId = contentMetadata.contentId;
            this.contentType = contentMetadata.contentType;
            this.contentStatus = contentMetadata.contentStatus;
            this.contentAccessAuthType = contentMetadata.contentAccessAuthType;
        }

        @Override
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setContentId(long contentId) {
            this.contentId = contentId;
            return this;
        }

        public Builder setContentType(@NonNull ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setContentStatus(@NonNull ContentStatus contentStatus) {
            this.contentStatus = contentStatus;
            return this;
        }

        public Builder setContentAccessAuthType(@NonNull ContentAccessAuthType contentAccessAuthType) {
            this.contentAccessAuthType = contentAccessAuthType;
            return this;
        }

        @Override
        public ContentMetadata build() {
            return new ContentMetadata(id, userId, contentId,
                    contentType, contentStatus, contentAccessAuthType);
        }
    }
}
