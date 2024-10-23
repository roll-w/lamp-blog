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

package space.lingu.lamp.web.domain.comment.service;

import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentDetailsMetadata;
import space.lingu.lamp.content.ContentPublisher;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.UncreatedContent;
import space.lingu.lamp.content.collection.ContentCollectionIdentity;
import space.lingu.lamp.content.collection.ContentCollectionProvider;
import space.lingu.lamp.content.collection.ContentCollectionType;
import space.lingu.lamp.content.common.ContentException;
import space.lingu.lamp.web.domain.comment.Comment;
import space.lingu.lamp.web.domain.comment.CommentDetailsMetadata;
import space.lingu.lamp.web.domain.comment.repository.CommentRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author RollW
 */
@Service
public class CommentService implements ContentPublisher, ContentCollectionProvider {
    public static final int COMMENT_ROOT_ID = 0;

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public ContentDetails publish(@NonNull UncreatedContent uncreatedContent,
                                  LocalDateTime timestamp)
            throws ContentException {
        ContentDetailsMetadata detailsMetadata =
                uncreatedContent.getMetadata();
        if (!(detailsMetadata instanceof CommentDetailsMetadata commentDetailsMetadata)) {
            throw new IllegalArgumentException("Metadata was not been serialized as comment metadata.");
        }

        // TODO: check parent id

        Comment comment = Comment
                .builder()
                .setType(commentDetailsMetadata.contentType())
                .setContent(uncreatedContent.getContent())
                .setUserId(uncreatedContent.getUserId())
                .setParentId(commentDetailsMetadata.parentId())
                .setCreateTime(timestamp.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                .setCommentOn(commentDetailsMetadata.contentId())
                .build();

        return commentRepository.insert(comment);
    }

    @Override
    public boolean supports(@NonNull ContentType contentType) {
        return contentType == ContentType.COMMENT;
    }

    @NonNull
    @Override
    public List<? extends ContentDetails> getContents(
            ContentCollectionIdentity contentCollectionIdentity) {
        return switch (contentCollectionIdentity.getContentCollectionType()) {
            case COMMENTS -> commentRepository.get();
            case ARTICLE_COMMENTS -> commentRepository.getComments(
                    contentCollectionIdentity.getContentCollectionId(),
                    ContentType.ARTICLE
            );
            default -> throw new UnsupportedOperationException("Unsupported collection type: " +
                    contentCollectionIdentity.getContentCollectionType());
        };
    }

    @Override
    public boolean supportsCollection(@NonNull ContentCollectionType contentCollectionType) {
        return switch (contentCollectionType) {
            case COMMENTS, ARTICLE_COMMENTS -> true;
            default -> false;
        };
    }
}
