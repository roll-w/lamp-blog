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

package space.lingu.lamp.web.domain.like;

import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

/**
 * @author RollW
 */
@DataTable(name = "user_like", indices = {
        @Index(value = {"user_id", "content_id", "content_type"}, unique = true)
})
public final class UserLike implements LongDataItem<UserLike> {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "time")
    private final long time;

    @DataColumn(name = "content_id")
    private final String contentId;

    @DataColumn(name = "content_type")
    private final ContentType contentType;

    @DataColumn(name = "like_type")
    private final LikeType likeType;

    public UserLike(@Nullable Long id,
                    long userId, long time,
                    String contentId,
                    ContentType contentType,
                    LikeType likeType) {
        this.id = id;
        this.userId = userId;
        this.time = time;
        this.contentId = contentId;
        this.contentType = contentType;
        this.likeType = likeType;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public long getUpdateTime() {
        return time;
    }

    public long getUserId() {
        return userId;
    }

    public long getTime() {
        return time;
    }

    public String getContentId() {
        return contentId;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public LikeType getLikeType() {
        return likeType;
    }

    @Override
    @NonNull
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.USER_LIKE;
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder implements LongEntityBuilder<UserLike> {
        private Long id;
        private long userId;
        private long time;
        private String contentId;
        private ContentType contentType;
        private LikeType likeType;

        public Builder() {
        }

        public Builder(UserLike userLike) {
            this.id = userLike.id;
            this.userId = userLike.userId;
            this.time = userLike.time;
            this.contentId = userLike.contentId;
            this.contentType = userLike.contentType;
            this.likeType = userLike.likeType;
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setTime(long time) {
            this.time = time;
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

        public Builder setLikeType(LikeType likeType) {
            this.likeType = likeType;
            return this;
        }

        public UserLike build() {
            return new UserLike(id, userId, time, contentId, contentType, likeType);
        }
    }
}
