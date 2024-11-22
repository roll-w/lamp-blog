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

package tech.lamprism.lampray.content.comment.service;

import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import tech.lamprism.lampray.content.ContentDetails;
import tech.lamprism.lampray.content.ContentDetailsMetadata;
import tech.lamprism.lampray.content.ContentPublisher;
import tech.lamprism.lampray.content.ContentType;
import tech.lamprism.lampray.content.UncreatedContent;
import tech.lamprism.lampray.content.collection.ContentCollectionIdentity;
import tech.lamprism.lampray.content.collection.ContentCollectionProvider;
import tech.lamprism.lampray.content.collection.ContentCollectionType;
import tech.lamprism.lampray.content.comment.CommentDetailsMetadata;
import tech.lamprism.lampray.content.comment.persistence.CommentDo;
import tech.lamprism.lampray.content.comment.persistence.CommentRepository;
import tech.lamprism.lampray.content.common.ContentException;

import java.time.OffsetDateTime;
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
                                  OffsetDateTime timestamp)
            throws ContentException {
        ContentDetailsMetadata detailsMetadata =
                uncreatedContent.getMetadata();
        if (!(detailsMetadata instanceof CommentDetailsMetadata commentDetailsMetadata)) {
            throw new IllegalArgumentException("Metadata was not been serialized as comment metadata.");
        }

        // TODO: check parent id
        CommentDo comment = CommentDo
                .builder()
                .setUserId(uncreatedContent.getUserId())
                .setParentId(commentDetailsMetadata.parentId())
                .setContent(uncreatedContent.getContent())
                .setCreateTime(timestamp)
                .setUpdateTime(timestamp)
                .setCommentOnId(commentDetailsMetadata.contentId())
                .setCommentOnType(commentDetailsMetadata.contentType())
                .build();

        return commentRepository.save(comment).lock();
    }

    @Override
    public boolean supports(@NonNull ContentType contentType) {
        return contentType == ContentType.COMMENT;
    }

    @NonNull
    @Override
    public List<? extends ContentDetails> getContents(
            ContentCollectionIdentity contentCollectionIdentity) {
        return getCommentsBy(contentCollectionIdentity)
                .stream()
                .map(CommentDo::lock)
                .toList();
    }

    @NonNull
    public List<CommentDo> getCommentsBy(
            ContentCollectionIdentity contentCollectionIdentity) {
        return switch (contentCollectionIdentity.getContentCollectionType()) {
            case COMMENTS -> commentRepository.findAll();
            case ARTICLE_COMMENTS -> commentRepository.findByContent(
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
