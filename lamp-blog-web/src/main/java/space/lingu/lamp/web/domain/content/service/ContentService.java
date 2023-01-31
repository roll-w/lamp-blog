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

import org.springframework.stereotype.Service;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.web.domain.content.BasicContentInfo;
import space.lingu.lamp.web.domain.content.ContentAccessCredentials;
import space.lingu.lamp.web.domain.content.ContentAccessService;
import space.lingu.lamp.web.domain.content.ContentAccessor;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import space.lingu.lamp.web.domain.content.permit.ContentPermitChecker;
import space.lingu.lamp.web.domain.content.permit.ContentPermitResult;
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository;

import java.util.Collection;
import java.util.Set;

/**
 * @author RollW
 */
@Service
public class ContentService implements ContentAccessService {
    private final Set<ContentAccessor<?>> contentAccessors;
    private final ContentPermitChecker contentPermitChecker;
    private final ContentMetadataRepository contentMetadataRepository;

    public ContentService(Set<ContentAccessor<?>> contentAccessors,
                          ContentPermitChecker contentPermitChecker,
                          ContentMetadataRepository contentMetadataRepository) {
        this.contentAccessors = contentAccessors;
        this.contentPermitChecker = contentPermitChecker;
        this.contentMetadataRepository = contentMetadataRepository;
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
        ContentPermitResult permitResult = contentPermitChecker.checkPermit(
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

    private ContentAccessor<?> getFirstAvailableOf(ContentType type) {
        return contentAccessors
                .stream()
                .filter(accessor -> accessor.supports(type))
                .findFirst()
                .orElseThrow(() ->
                        new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND,
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
}
