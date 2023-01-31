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

package space.lingu.lamp.web.domain.review.event;

import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class DefaultContentReviewStatusMarker implements ReviewStatusMarker {
    private final ContentMetadataRepository contentMetadataRepository;

    public DefaultContentReviewStatusMarker(ContentMetadataRepository contentMetadataRepository) {
        this.contentMetadataRepository = contentMetadataRepository;
    }

    @Override
    public void markAsReviewed(ContentType reviewType, String contentId) {
        contentMetadataRepository.updateStatus(contentId, reviewType, ContentStatus.PUBLISHED);
    }

    @Override
    public void markAsRejected(ContentType contentType, String contentId, String reason) {
        contentMetadataRepository.updateStatus(contentId, contentType,
                ContentStatus.REVIEW_REJECTED);
    }

    @NonNull
    @Override
    public List<ContentType> getSupportedReviewTypes() {
        return List.of(ContentType.values());
    }
}
