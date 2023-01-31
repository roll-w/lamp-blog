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

package space.lingu.lamp.web.domain.article;

import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.DataItem;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;
import space.lingu.light.SQLDataType;

/**
 * @author RollW
 */
@SuppressWarnings("ClassCanBeRecord")
@DataTable(tableName = "article", indices = {
        @Index(value = {"user_id", "title"}, unique = true)
})
public class Article implements DataItem, ContentDetails {
    // TODO: article

    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "title")
    private final String title;

    @DataColumn(name = "cover")
    private final String cover;

    @DataColumn(name = "content", dataType = SQLDataType.LONGTEXT)
    private final String content;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    public Article(Long id, long userId, String title, String cover,
                   String content,
                   long createTime, long updateTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.cover = cover;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Long getId() {
        return id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    @Nullable
    public String getContent() {
        return content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public String getCover() {
        return cover;
    }

    public Article fork(Long id) {
        return new Article(id, userId, title, cover,
                content, createTime, updateTime
        );
    }

    public Builder toBuilder() {
        return new Builder()
                .setId(id)
                .setUserId(userId)
                .setTitle(title)
                .setCover(cover)
                .setContent(content)
                .setCreateTime(createTime)
                .setUpdateTime(updateTime);
    }

    public static Builder builder() {
        return new Builder();
    }

    @NonNull
    @Override
    public String getContentId() {
        if (id == null) {
            throw new IllegalStateException("The article has not been saved.");
        }
        return String.valueOf(id);
    }

    @NonNull
    @Override
    public ContentType getContentType() {
        return ContentType.ARTICLE;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    public final static class Builder {
        private Long id;
        private long userId;
        private String title;
        private String cover;
        private String content;
        private long createTime;
        private long updateTime;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
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

        public Builder setCover(String cover) {
            this.cover = cover;
            return this;
        }

        public Article build() {
            return new Article(id, userId, title, cover, content,
                    createTime, updateTime);
        }
    }
}
