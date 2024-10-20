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
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.content.ContentAssociated;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentIdentity;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;
import space.lingu.light.SQLDataType;
import tech.rollw.common.web.system.SystemResourceKind;

/**
 * @author RollW
 */
@DataTable(name = "comment")
public class Comment implements LongDataItem<Comment>, ContentDetails, ContentAssociated {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    // TODO: rename to commentOnId
    @DataColumn(name = "comment_on_id")
    private final long commentOn;

    @DataColumn(name = "parent_id")
    private final long parentId;

    @DataColumn(name = "content", dataType = SQLDataType.LONGTEXT)
    private final String content;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    /**
     * Comment on which type of content.
     */
    // TODO: rename to commentOnType
    @DataColumn(name = "type")
    private final ContentType type;

    @DataColumn(name = "status", nullable = false)
    @NonNull
    private final CommentStatus commentStatus;

    private final ContentIdentity associatedContent;

    public Comment(Long id, long userId, long commentOn,
                   long parentId, String content,
                   long createTime, long updateTime,
                   ContentType type, @NonNull CommentStatus commentStatus) {
        this.id = id;
        this.userId = userId;
        this.commentOn = commentOn;
        this.parentId = parentId;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.type = type;
        this.commentStatus = commentStatus;
        this.associatedContent = ContentIdentity.of(commentOn, type);
    }

    public Long getId() {
        return id;
    }

    @Override
    public Long getResourceId() {
        return id;
    }

    @Override
    public long getContentId() {
        return id;
    }

    @NonNull
    @Override
    public ContentType getContentType() {
        return ContentType.COMMENT;
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

    public long getParentId() {
        return parentId;
    }

    public long getCommentOn() {
        return commentOn;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }

    public ContentType getType() {
        return type;
    }

    @NonNull
    public CommentStatus getCommentStatus() {
        return commentStatus;
    }

    @Override
    public ContentIdentity getAssociatedContent() {
        return associatedContent;
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
        return LampSystemResourceKind.COMMENT;
    }

    public static class Builder implements LongEntityBuilder<Comment> {
        private Long id;
        private long userId;
        private long commentOn;
        private long parentId;
        private String content;
        private long createTime;
        private long updateTime;
        private ContentType type;
        @NonNull
        private CommentStatus commentStatus;

        public Builder(Comment comment) {
            this.id = comment.id;
            this.userId = comment.userId;
            this.commentOn = comment.commentOn;
            this.parentId = comment.parentId;
            this.content = comment.content;
            this.createTime = comment.createTime;
            this.updateTime = comment.updateTime;
            this.type = comment.type;
            this.commentStatus = comment.commentStatus;
        }

        public Builder() {
            commentStatus = CommentStatus.NONE;
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

        public Builder setCommentOn(long commentOn) {
            this.commentOn = commentOn;
            return this;
        }

        public Builder setParentId(long parentId) {
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

        public Builder setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setType(ContentType type) {
            this.type = type;
            return this;
        }

        public Builder setCommentStatus(@NonNull CommentStatus commentStatus) {
            this.commentStatus = commentStatus;
            return this;
        }

        @Override
        public Comment build() {
            return new Comment(
                    id, userId, commentOn,
                    parentId, content,
                    createTime, updateTime, type, commentStatus
            );
        }
    }

}
