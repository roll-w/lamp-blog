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

package space.lingu.lamp.web.domain.favorite;

import space.lingu.NonNull;
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.content.ContentAssociated;
import space.lingu.lamp.content.ContentIdentity;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.LightIgnore;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

/**
 * @author RollW
 */
@DataTable(name = "favorite_item")
public class FavoriteItem implements LongDataItem<FavoriteItem>, ContentAssociated {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "group_id")
    private final long favoriteGroupId;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "content_id")
    private final long contentId;

    @DataColumn(name = "content_type")
    private final ContentType contentType;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    @DataColumn(name = "deleted")
    private final boolean deleted;

    @LightIgnore
    private final ContentIdentity associatedContent;

    public FavoriteItem(Long id, long favoriteGroupId, long userId,
                        long contentId, ContentType contentType,
                        long createTime, long updateTime, boolean deleted) {
        this.id = id;
        this.favoriteGroupId = favoriteGroupId;
        this.userId = userId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
        this.associatedContent = ContentIdentity.of(contentId, contentType);
    }

    @Override
    public Long getId() {
        return id;
    }

    public long getFavoriteGroupId() {
        return favoriteGroupId;
    }

    public long getUserId() {
        return userId;
    }

    public long getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public ContentIdentity getAssociatedContent() {
        return associatedContent;
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.FAVORITE_ITEM;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder implements LongEntityBuilder<FavoriteItem> {
        private Long id;
        private long favoriteGroupId;
        private long userId;
        private long contentId;
        private ContentType contentType;
        private long createTime;
        private long updateTime;
        private boolean deleted;

        private Builder() {
        }

        private Builder(FavoriteItem favoriteitem) {
            this.id = favoriteitem.id;
            this.favoriteGroupId = favoriteitem.favoriteGroupId;
            this.userId = favoriteitem.userId;
            this.contentId = favoriteitem.contentId;
            this.contentType = favoriteitem.contentType;
            this.createTime = favoriteitem.createTime;
            this.updateTime = favoriteitem.updateTime;
            this.deleted = favoriteitem.deleted;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setFavoriteGroupId(long favoriteGroupId) {
            this.favoriteGroupId = favoriteGroupId;
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

        public Builder setContentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public FavoriteItem build() {
            return new FavoriteItem(id, favoriteGroupId, userId, contentId,
                    contentType, createTime, updateTime, deleted);
        }
    }
}