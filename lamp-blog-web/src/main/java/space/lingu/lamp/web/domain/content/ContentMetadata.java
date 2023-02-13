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
import space.lingu.lamp.DataItem;
import space.lingu.light.Constructor;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(name = "content_metadata", indices =
@Index(value = {"content_id", "type"}, unique = true))
@SuppressWarnings({"ClassCanBeRecord", "unused"})
public class ContentMetadata implements DataItem {
    // only maintains the metadata of the content.
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "content_id", nullable = false)
    @NonNull
    private final String contentId;

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
                           @NonNull String contentId, @NonNull ContentType contentType,
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
        if (contentId == null) {
            throw new NullPointerException("contentId is null");
        }
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

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    @NonNull
    public String getContentId() {
        return contentId;
    }

    @NonNull
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

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private long userId;
        private String contentId;
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

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setContentId(@NonNull String contentId) {
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

        public ContentMetadata build() {
            return new ContentMetadata(id, userId, contentId,
                    contentType, contentStatus, contentAccessAuthType);
        }
    }


}
