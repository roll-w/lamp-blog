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
import space.lingu.lamp.DataEntity;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import tech.rollw.common.web.system.SystemResourceKind;

import java.time.LocalDateTime;

/**
 * @author RollW
 */
public class FavoriteGroup implements DataEntity<Long> {
    private final Long id;
    private final String name;
    private final long userId;
    private final boolean isPublic;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean deleted;

    public FavoriteGroup(Long id, String name, long userId,
                         boolean isPublic,
                         LocalDateTime createTime, LocalDateTime updateTime,
                         boolean deleted) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.isPublic = isPublic;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
    }

    @Override
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getUserId() {
        return userId;
    }

    public boolean isPublic() {
        return isPublic;
    }

    @NonNull
    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @NonNull
    @Override
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.FAVORITE_GROUP;
    }

    public static Builder builder() {
        return new Builder();
    }

    public final static class Builder implements LongEntityBuilder<FavoriteGroup> {
        private Long id;
        private String name;
        private long userId;
        private boolean isPublic;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean deleted;

        public Builder() {
        }

        public Builder(FavoriteGroup favoriteGroup) {
            this.id = favoriteGroup.id;
            this.name = favoriteGroup.name;
            this.userId = favoriteGroup.userId;
            this.isPublic = favoriteGroup.isPublic;
            this.createTime = favoriteGroup.createTime;
            this.updateTime = favoriteGroup.updateTime;
            this.deleted = favoriteGroup.deleted;
        }

        @Override
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setPublic(boolean aPublic) {
            isPublic = aPublic;
            return this;
        }

        public Builder setCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        @Override
        public FavoriteGroup build() {
            return new FavoriteGroup(id, name, userId, isPublic,
                    createTime, updateTime, deleted);
        }
    }
}
