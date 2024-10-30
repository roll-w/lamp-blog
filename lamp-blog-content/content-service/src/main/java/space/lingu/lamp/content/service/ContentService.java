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

package space.lingu.lamp.content.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import space.lingu.lamp.content.Content;
import space.lingu.lamp.content.ContentAccessAuthType;
import space.lingu.lamp.content.ContentAccessCredentials;
import space.lingu.lamp.content.ContentAccessService;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentMetadata;
import space.lingu.lamp.content.ContentMetadataDetails;
import space.lingu.lamp.content.ContentProvider;
import space.lingu.lamp.content.ContentProviderFactory;
import space.lingu.lamp.content.ContentPublishProvider;
import space.lingu.lamp.content.ContentPublisher;
import space.lingu.lamp.content.ContentStatus;
import space.lingu.lamp.content.ContentSupportableUtils;
import space.lingu.lamp.content.ContentTrait;
import space.lingu.lamp.content.UncreatedContent;
import space.lingu.lamp.content.UncreatedContentPreChecker;
import space.lingu.lamp.content.collection.ContentCollectionIdentity;
import space.lingu.lamp.content.collection.ContentCollectionProvider;
import space.lingu.lamp.content.collection.ContentCollectionProviderFactory;
import space.lingu.lamp.content.collection.ContentCollectionType;
import space.lingu.lamp.content.common.ContentErrorCode;
import space.lingu.lamp.content.common.ContentException;
import space.lingu.lamp.content.permit.ContentPermitChecker;
import space.lingu.lamp.content.permit.ContentPermitResult;
import space.lingu.lamp.content.persistence.ContentMetadataDo;
import space.lingu.lamp.content.persistence.ContentMetadataRepository;
import space.lingu.lamp.content.publish.ContentPublishCallback;
import tech.rollw.common.web.CommonErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.UnsupportedKindException;
import tech.rollw.common.web.system.paged.PageableContext;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

/**
 * @author RollW
 */
@Service
public class ContentService implements ContentAccessService,
        ContentPublishProvider, ContentCollectionProviderFactory {
    private static final Logger logger = LoggerFactory.getLogger(ContentService.class);
    private final List<ContentPublisher> contentPublishers;
    private final List<UncreatedContentPreChecker> uncreatedContentPreCheckers;
    private final List<ContentCollectionProvider> contentCollectionProviders;
    private final List<ContentPublishCallback> contentPublishCallbacks;

    private final ContentProviderFactory contentProviderFactory;
    private final ContentPermitChecker contentPermitChecker;
    private final ContentMetadataRepository contentMetadataRepository;
    private final ContextThreadAware<PageableContext> pageableContextThreadAware;

    public ContentService(List<ContentPublisher> contentPublishers,
                          List<UncreatedContentPreChecker> uncreatedContentPreCheckers,
                          List<ContentCollectionProvider> contentCollectionProviders,
                          List<ContentPublishCallback> contentPublishCallbacks,
                          ContentProviderFactory contentProviderFactory,
                          ContentPermitChecker contentPermitChecker,
                          ContentMetadataRepository contentMetadataRepository,
                          ContextThreadAware<PageableContext> pageableContextThreadAware) {
        this.contentPublishers = contentPublishers;
        this.uncreatedContentPreCheckers = uncreatedContentPreCheckers;
        this.contentCollectionProviders = contentCollectionProviders;
        this.contentPublishCallbacks = contentPublishCallbacks;
        this.contentProviderFactory = contentProviderFactory;
        this.contentPermitChecker = contentPermitChecker;
        this.contentMetadataRepository = contentMetadataRepository;
        this.pageableContextThreadAware = pageableContextThreadAware;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ContentDetails openContent(ContentTrait contentTrait,
                                      ContentAccessCredentials contentAccessCredentials) throws ContentException {
        ContentMetadataDo metadata = contentMetadataRepository
                .findByContent(contentTrait)
                .orElse(null);
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        ErrorCode errorCode = fromContentStatus(metadata.getContentStatus());
        if (errorCode.failed()) {
            throw new ContentException(errorCode);
        }
        ContentPermitResult permitResult = contentPermitChecker.checkAccessPermit(
                Content.of(metadata.getUserId(), contentTrait),
                metadata.getContentAccessAuthType(),
                contentAccessCredentials
        );
        if (!permitResult.isPermitted()) {
            throw new ContentException(
                    selectFirst(permitResult.errors())
            );
        }
        ContentProvider provider = contentProviderFactory.getContentProvider(
                contentTrait.getContentType());
        return provider.getContentDetails(contentTrait);
    }

    /**
     * @inheritDoc
     */
    @Override
    public ContentDetails getContentDetails(ContentTrait contentTrait)
            throws ContentException {
        return getContentMetadataDetails(contentTrait);
    }

    @Override
    public ContentMetadataDetails<?> getContentMetadataDetails(ContentTrait contentTrait)
            throws ContentException {
        ContentMetadataDo metadata = contentMetadataRepository
                .findByContent(contentTrait)
                .orElse(null);
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        ContentProvider provider = contentProviderFactory.getContentProvider(
                contentTrait.getContentType());
        ContentDetails contentDetails = provider
                .getContentDetails(contentTrait);
        return new ContentMetadataDetails<>(contentDetails, metadata.lock());
    }

    /**
     * @inheritDoc
     */
    public ContentStatus getContentStatus(ContentTrait contentTrait)
            throws ContentException {
        ContentMetadataDo metadata = contentMetadataRepository
                .findByContent(contentTrait)
                .orElse(null);
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return metadata.getContentStatus();
    }

    private ErrorCode fromContentStatus(ContentStatus status) {
        return switch (status) {
            case PUBLISHED -> CommonErrorCode.SUCCESS;
            case DRAFT -> CommonErrorCode.ERROR_NOT_FOUND;
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
        OffsetDateTime timestamp = OffsetDateTime.now();
        ContentSupportableUtils.findAllSupportable(uncreatedContentPreCheckers,
                        uncreatedContent.getContentType())
                .forEach(checker ->
                        checker.checkUncreatedContent(uncreatedContent)
                );
        ContentPublisher contentPublisher = ContentSupportableUtils.findSupportableNonNull(
                contentPublishers,
                uncreatedContent.getContentType());
        ContentDetails contentDetails = contentPublisher.publish(
                uncreatedContent,
                timestamp
        );
        ContentMetadataDo.Builder contentMetadataBuilder = ContentMetadataDo
                .builder()
                .setContentId(contentDetails.getContentId())
                .setContentType(contentDetails.getContentType())
                .setUserId(contentDetails.getUserId())
                .setContentAccessAuthType(ContentAccessAuthType.PUBLIC);
        ContentStatus contentStatus = ContentStatus.PUBLISHED;
        for (ContentPublishCallback contentPublishCallback :
                contentPublishCallbacks) {
            contentStatus = contentStatus.plus(
                    contentPublishCallback.publish(contentDetails)
            );
        }
        contentMetadataBuilder.setContentStatus(contentStatus);
        contentMetadataRepository.save(contentMetadataBuilder.build());
        return contentDetails;
    }

    private <T extends ContentDetails> List<ContentMetadataDetails<T>> pairWith(
            List<T> contentDetails,
            List<ContentMetadata> contentMetadata) {
        return contentDetails.stream().map(details -> {
            ContentMetadata metadata = contentMetadata.stream()
                    .filter(m -> m.getContentId() == details.getContentId())
                    .findFirst()
                    .orElseThrow(() -> new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND));
            return new ContentMetadataDetails<>(details, metadata);
        }).toList();
    }

    @Override
    public ContentCollectionProvider getContentCollectionProvider(
            ContentCollectionType contentCollectionType) {
        return contentCollectionProviders.stream()
                .filter(contentCollectionProvider -> contentCollectionProvider
                        .supportsCollection(contentCollectionType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedKindException(
                        "Unsupported content collection type: " + contentCollectionType));
    }

    @Override
    public List<ContentMetadataDetails<? extends ContentDetails>> getContents(
            ContentCollectionIdentity contentCollectionIdentity,
            ContentAccessCredentials contentAccessCredentials) {
        // TODO: impl
        return List.of();
    }

    @Override
    public List<ContentMetadataDetails<? extends ContentDetails>> getContents(
            ContentCollectionIdentity contentCollectionIdentity) {
        // TODO: impl
        return List.of();
    }
}
