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
import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.content.*;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionIdentity;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionProvider;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionProviderFactory;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import space.lingu.lamp.web.domain.content.permit.ContentPermitChecker;
import space.lingu.lamp.web.domain.content.permit.ContentPermitResult;
import space.lingu.lamp.web.domain.content.publish.ContentPublishCallback;
import space.lingu.lamp.web.domain.content.publish.ContentPublishFilter;
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository;
import tech.rollw.common.web.CommonErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.UnsupportedKindException;
import tech.rollw.common.web.system.paged.PageableContext;

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
    private final List<ContentCollectionProvider> contentCollectionProviders;

    private final List<ContentPublishFilter> contentPublishFilters;
    private final List<ContentPublishCallback> contentPublishCallbacks;

    private final ContentProviderFactory contentProviderFactory;
    private final ContentPermitChecker contentPermitChecker;
    private final ContentMetadataRepository contentMetadataRepository;
    private final ContextThreadAware<PageableContext> pageableContextThreadAware;

    public ContentService(List<ContentPublisher> contentPublishers,
                          List<ContentCollectionProvider> contentCollectionProviders,
                          List<ContentPublishFilter> contentPublishFilters,
                          List<ContentPublishCallback> contentPublishCallbacks,
                          ContentProviderFactory contentProviderFactory,
                          ContentPermitChecker contentPermitChecker,
                          ContentMetadataRepository contentMetadataRepository,
                          ContextThreadAware<PageableContext> pageableContextThreadAware) {
        this.contentPublishers = contentPublishers;
        this.contentCollectionProviders = contentCollectionProviders;
        this.contentPublishFilters = contentPublishFilters;
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
        ContentMetadata metadata = contentMetadataRepository.getById(
                contentTrait.getContentId(),
                contentTrait.getContentType()
        );
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
        ContentMetadata metadata = contentMetadataRepository.getById(
                contentTrait.getContentId(),
                contentTrait.getContentType()
        );
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        ContentProvider provider = contentProviderFactory.getContentProvider(
                contentTrait.getContentType());
        ContentDetails contentDetails = provider
                .getContentDetails(contentTrait);
        return new ContentMetadataDetails<>(contentDetails, metadata);
    }

    /**
     * @inheritDoc
     */
    public ContentStatus getContentStatus(ContentTrait contentTrait)
            throws ContentException {
        ContentMetadata metadata = contentMetadataRepository.getById(
                contentTrait.getContentId(), contentTrait.getContentType());
        if (metadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return metadata.getContentStatus();
    }

    private ContentPublisher getFirstAvailablePublisherOf(ContentType type) {
        return contentPublishers
                .stream()
                .filter(accessor -> accessor.supports(type))
                .findFirst()
                .orElseThrow(() -> {
                    logger.warn("Unsupported/Unregistered content type of ContentType.{}", type);
                    return new ContentException(
                            CommonErrorCode.ERROR_NOT_FOUND,
                            "Unsupported content type of " + type
                    );
                });
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
        long timestamp = System.currentTimeMillis();
        for (ContentPublishFilter contentPublishFilter :
                contentPublishFilters) {
            ErrorCode errorCode = contentPublishFilter.filter(uncreatedContent);
            if (errorCode.failed()) {
                throw new ContentException(errorCode);
            }
        }
        ContentPublisher contentPublisher = getFirstAvailablePublisherOf(
                uncreatedContent.getContentType());
        ContentDetails contentDetails = contentPublisher.publish(
                uncreatedContent,
                timestamp
        );
        ContentMetadata.Builder contentMetadataBuilder = ContentMetadata
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
        contentMetadataRepository.insert(contentMetadataBuilder.build());
        return contentDetails;
    }

    private ContentMetadata tryGetContentMetadata(ContentTrait contentTrait) {
        ContentMetadata contentMetadata = contentMetadataRepository.getById(
                contentTrait.getContentId(), contentTrait.getContentType());
        if (contentMetadata == null) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return contentMetadata;
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
