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

package space.lingu.lamp.web.domain.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.web.domain.content.BasicContentInfo;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentAccessCredentials;
import space.lingu.lamp.web.domain.content.ContentAccessService;
import space.lingu.lamp.web.domain.content.ContentAccessor;
import space.lingu.lamp.web.domain.content.ContentDeleteService;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentPublishService;
import space.lingu.lamp.web.domain.content.ContentPublisher;
import space.lingu.lamp.web.domain.content.UncreatedContent;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import space.lingu.lamp.web.domain.content.event.ContentStatusEvent;
import space.lingu.lamp.web.domain.content.permit.ContentPermitChecker;
import space.lingu.lamp.web.domain.content.permit.ContentPermitResult;
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.common.NotReviewedException;
import space.lingu.lamp.web.domain.review.service.ReviewService;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author RollW
 */
@Service
public class ContentService implements ContentAccessService,
        ContentPublishService, ContentDeleteService {
    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);

    private final Set<ContentAccessor<?>> contentAccessors;
    private final Set<ContentPublisher> contentPublishers;
    private final ContentPermitChecker contentPermitChecker;
    private final ContentMetadataRepository contentMetadataRepository;
    private final ReviewService reviewService;
    private final ApplicationEventPublisher eventPublisher;

    public ContentService(Set<ContentAccessor<?>> contentAccessors,
                          Set<ContentPublisher> contentPublishers,
                          ContentPermitChecker contentPermitChecker,
                          ContentMetadataRepository contentMetadataRepository,
                          ReviewService reviewService,
                          ApplicationEventPublisher eventPublisher) {
        this.contentAccessors = contentAccessors;
        this.contentPublishers = contentPublishers;
        this.contentPermitChecker = contentPermitChecker;
        this.contentMetadataRepository = contentMetadataRepository;
        this.reviewService = reviewService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ContentDetails openContent(String contentId, ContentType contentType,
                                      ContentAccessCredentials contentAccessCredentials) throws ContentException {
        ContentMetadata metadata = contentMetadataRepository.getById(contentId, contentType);
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        ErrorCode errorCode = fromContentStatus(metadata.getContentStatus());
        if (errorCode.failed()) {
            throw new ContentException(errorCode);
        }
        ContentPermitResult permitResult = contentPermitChecker.checkAccessPermit(
                new BasicContentInfo(metadata.getUserId(), contentId, contentType),
                metadata.getContentAccessAuthType(),
                contentAccessCredentials
        );
        if (!permitResult.isPermitted()) {
            throw new ContentException(
                    selectFirst(permitResult.errors())
            );
        }
        ContentAccessor<?> accessor = getFirstAvailableOf(contentType);
        return accessor.getContent(contentId, contentType);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ContentDetails getContentDetails(String contentId,
                                            ContentType contentType) throws ContentException {
        ContentMetadata metadata = contentMetadataRepository.getById(contentId, contentType);
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        ContentAccessor<?> accessor = getFirstAvailableOf(contentType);
        return accessor.getContent(contentId, contentType);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ContentStatus getContentStatus(String contentId, ContentType contentType)
            throws ContentException {
        ContentMetadata metadata = contentMetadataRepository.getById(contentId, contentType);
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return metadata.getContentStatus();
    }

    @Override
    public List<ContentStatus> getContentStatuses(List<String> contentIds,
                                                  ContentType contentType)
            throws ContentException {

        return contentMetadataRepository.getStatusByIds(contentIds, contentType);
    }

    @Override
    public List<ContentStatus> getContentStatuses(List<? extends Content> contents)
            throws ContentException {

        return null;
    }

    private ContentAccessor<?> getFirstAvailableOf(ContentType type) {
        return contentAccessors
                .stream()
                .filter(accessor -> accessor.supports(type))
                .findFirst()
                .orElseThrow(() ->
                        new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND,
                                "Unsupported content type of " + type));
    }

    private ContentPublisher getFirstAvailablePublisherOf(ContentType type) {
        return contentPublishers
                .stream()
                .filter(accessor -> accessor.supports(type))
                .findFirst()
                .orElseThrow(() -> new ContentException(
                        ContentErrorCode.ERROR_CONTENT_NOT_FOUND,
                        "Unsupported content type of " + type));
    }

    private ErrorCode fromContentStatus(ContentStatus status) {
        return switch (status) {
            case PUBLISHED -> CommonErrorCode.SUCCESS;
            case REVIEWING -> ContentErrorCode.ERROR_CONTENT_REVIEWING;
            case HIDE -> ContentErrorCode.ERROR_CONTENT_HIDE;
            case FORBIDDEN, REVIEW_REJECTED -> ContentErrorCode.ERROR_CONTENT_FORBIDDEN;
            case DELETED -> ContentErrorCode.ERROR_CONTENT_DELETED;
        };
    }

    private static ErrorCode selectFirst(Collection<ErrorCode> errorCodes) {
        return errorCodes.iterator().next();
    }

    @Override
    public ContentDetails publishContent(UncreatedContent uncreatedContent) throws ContentException {
        long timestamp = System.currentTimeMillis();
        ContentPublisher contentPublisher = getFirstAvailablePublisherOf(
                uncreatedContent.getContentType());
        ContentDetails contentDetails = contentPublisher.publish(
                uncreatedContent,
                timestamp
        );
        ContentMetadata contentMetadata = ContentMetadata.builder()
                .setContentId(contentDetails.getContentId())
                .setContentType(contentDetails.getContentType())
                .setUserId(contentDetails.getUserId())
                .setContentStatus(ContentStatus.REVIEWING)
                .setContentAccessAuthType(ContentAccessAuthType.PUBLIC)
                .build();
        contentMetadataRepository.insert(contentMetadata);
        ContentStatusEvent<?> event = new ContentStatusEvent<>(
                contentDetails,
                timestamp,
                null,
                ContentStatus.REVIEWING
        );
        eventPublisher.publishEvent(event);

        try {
            ReviewJob reviewJob = reviewService.assignReviewer(contentDetails);
            logger.debug("Content review job assigned: {}.", reviewJob);
        } catch (NotReviewedException e) {
            logger.debug("Content review job already assigned: {}.", e.getReviewJob());
        }
        return contentDetails;
    }

    @Override
    public void deleteContent(ContentType contentType, String contentId) {
        tryUpdateContentStatus(contentId, contentType, ContentStatus.DELETED);
    }

    @Override
    public void forbiddenContent(ContentType contentType, String contentId) {
        tryUpdateContentStatus(contentId, contentType, ContentStatus.FORBIDDEN);
    }

    @Override
    public void restoreContent(ContentType contentType, String contentId) {
        ContentMetadata contentMetadata =
                tryGetContentMetadata(contentId, contentType);
        if (contentMetadata.getContentStatus() != ContentStatus.DELETED) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_DELETED);
        }
    }

    private void tryUpdateContentStatus(String contentId,
                                        ContentType contentType,
                                        ContentStatus newStatus) {
        ContentMetadata contentMetadata =
                tryGetContentMetadata(contentId, contentType);
        tryUpdateContentStatus(contentMetadata, newStatus);
    }

    private void tryUpdateContentStatus(ContentMetadata contentMetadata,
                                        ContentStatus newStatus) {
        long timestamp = System.currentTimeMillis();
        if (contentMetadata.getContentStatus() == newStatus) {
            return;
        }
        contentMetadataRepository.updateStatus(
                contentMetadata.getContentId(),
                contentMetadata.getContentType(),
                newStatus
        );
        BasicContentInfo contentInfo = new BasicContentInfo(
                contentMetadata.getUserId(),
                contentMetadata.getContentId(),
                contentMetadata.getContentType()
        );
        ContentStatusEvent<?> event = new ContentStatusEvent<>(
                contentInfo,
                timestamp,
                contentMetadata.getContentStatus(),
                newStatus
        );
        eventPublisher.publishEvent(event);
    }

    private ContentMetadata tryGetContentMetadata(String contentId,
                                                  ContentType contentType) {
        ContentMetadata contentMetadata =
                contentMetadataRepository.getById(contentId, contentType);
        if (contentMetadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return contentMetadata;
    }
}
