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

package space.lingu.lamp.web.domain.comment;

import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(tableName = "comment")
public class Comment implements ContentDetails {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "comment_on_id")
    private final String commentOn;

    @DataColumn(name = "parent_id")
    @Nullable
    private final Long parentId;

    @DataColumn(name = "content")
    private final String content;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "type")
    private final CommentType type;

    public Comment(Long id, long userId, String commentOn,
                   @Nullable Long parentId, String content, long createTime,
                   CommentType type) {
        this.id = id;
        this.userId = userId;
        this.commentOn = commentOn;
        this.parentId = parentId;
        this.content = content;
        this.createTime = createTime;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    @NonNull
    @Override
    public String getContentId() {
        if (id == null) {
            throw new IllegalStateException("The comment has not been saved.");
        }
        return String.valueOf(id);
    }

    @NonNull
    @Override
    public ContentType getContentType() {
        return ContentType.COMMENT;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getContent() {
        return content;
    }

    @Nullable
    public Long getParentId() {
        return parentId;
    }

    public String getCommentOn() {
        return commentOn;
    }

    public long getCreateTime() {
        return createTime;
    }

    public CommentType getType() {
        return type;
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
        private String commentOn;
        private Long parentId;
        private String content;
        private long createTime;
        private CommentType type;

        public Builder(Comment comment) {
            this.id = comment.id;
            this.userId = comment.userId;
            this.commentOn = comment.commentOn;
            this.parentId = comment.parentId;
            this.content = comment.content;
            this.createTime = comment.createTime;
            this.type = comment.type;
        }

        public Builder() {
        }

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setCommentOn(String commentOn) {
            this.commentOn = commentOn;
            return this;
        }

        public Builder setParentId(Long parentId) {
            this.parentId = parentId;
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

        public Builder setType(CommentType type) {
            this.type = type;
            return this;
        }

        public Comment build() {
            return new Comment(id, userId, commentOn,
                    parentId, content, createTime, type);
        }
    }

}
