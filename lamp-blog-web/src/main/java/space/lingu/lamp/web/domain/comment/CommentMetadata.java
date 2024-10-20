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
import space.lingu.lamp.content.Content;
import space.lingu.lamp.content.ContentType;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;

/**
 * Set the comment permission for a content.
 *
 * @author RollW
 */
@DataTable(name = "comment_metadata", indices = {
        @Index(value = {"content_id", "content_type"}, unique = true),
})
public class CommentMetadata {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

    @DataColumn(name = "content_id")
    @NonNull
    private final String contentId;

    @DataColumn(name = "content_type")
    @NonNull
    private final ContentType contentType;

    @DataColumn(name = "permission")
    private final ContentCommentPermission contentCommentPermission;

    public CommentMetadata(@Nullable Long id,
                           long userId,
                           @NonNull String contentId,
                           @NonNull ContentType contentType,
                           ContentCommentPermission contentCommentPermission) {
        this.id = id;
        this.userId = userId;
        this.contentId = contentId;
        this.contentType = contentType;
        this.contentCommentPermission = contentCommentPermission;
    }

    @Nullable
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

    public ContentCommentPermission getCommentPermission() {
        return contentCommentPermission;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static CommentMetadata defaultOf(Content content) {
        return builder()
                .setUserId(content.getUserId())
                .setId(-1L)
                .setContentType(content.getContentType())
                .setCommentPermission(ContentCommentPermission.PUBLIC)
                .build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Long id;
        private long userId;
        private String contentId;
        private ContentType contentType;
        private ContentCommentPermission contentCommentPermission;

        public Builder() {
        }

        public Builder(CommentMetadata commentMetadata) {
            this.id = commentMetadata.id;
            this.userId = commentMetadata.userId;
            this.contentId = commentMetadata.contentId;
            this.contentType = commentMetadata.contentType;
            this.contentCommentPermission = commentMetadata.contentCommentPermission;
        }

        public Builder setId(@Nullable Long id) {
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

        public Builder setCommentPermission(ContentCommentPermission contentCommentPermission) {
            this.contentCommentPermission = contentCommentPermission;
            return this;
        }

        public CommentMetadata build() {
            return new CommentMetadata(id, userId, contentId,
                    contentType, contentCommentPermission);
        }
    }
}
