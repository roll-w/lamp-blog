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

package space.lingu.lamp.web.domain.content.event;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.content.BasicContentInfo;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentNotifier;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.event.OnReviewStateChangeEvent;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;

/**
 * Converts {@link OnReviewStateChangeEvent} to {@link ContentStatusEvent}.
 *
 * @author RollW
 */
@Component
public class OnReviewStateChangeListener implements ApplicationListener<OnReviewStateChangeEvent> {
    private final ContentMetadataRepository contentMetadataRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Multimap<ContentType, ContentNotifier> contentNotifiers =
            Multimaps.newSetMultimap(new EnumMap<>(ContentType.class), HashSet::new);

    private static final Logger logger =
            LoggerFactory.getLogger(OnReviewStateChangeListener.class);

    public OnReviewStateChangeListener(
            List<ContentNotifier> contentNotifiers,
            ContentMetadataRepository contentMetadataRepository,
            ApplicationEventPublisher eventPublisher) {
        this.contentMetadataRepository = contentMetadataRepository;
        this.eventPublisher = eventPublisher;
        contentNotifiers.forEach(
                notifier -> notifier.getSupportedContentTypes().forEach(
                        type -> this.contentNotifiers.put(type, notifier)
                ));
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnReviewStateChangeEvent event) {
        ReviewJob reviewJob = event.getReviewJob();
        ContentMetadata metadata = contentMetadataRepository.getById(
                reviewJob.getReviewContentId(),
                reviewJob.getType()
        );
        if (metadata == null) {
            // means metadata not has been successfully created
            logger.error("Maybe a bug, ContentMetadata not found for review job: {}.",
                    reviewJob);
            return;
        }
        BasicContentInfo contentInfo = new BasicContentInfo(
                metadata.getUserId(),
                metadata.getContentId(),
                metadata.getContentType()
        );
        ContentStatus currentStatus = toContentStatus(event.getCurrentStatus());
        ContentStatus previousStatus = toContentStatus(event.getPreviousStatus());
        contentMetadataRepository.updateStatus(
                contentInfo.getContentId(),
                contentInfo.getContentType(),
                currentStatus
        );
        ContentStatusEvent<?> contentStatusEvent = new ContentStatusEvent<>(
                contentInfo, reviewJob.getReviewTime(),
                previousStatus, currentStatus);
        eventPublisher.publishEvent(contentStatusEvent);
    }

    private static ContentStatus toContentStatus(ReviewStatus status) {
        if (status == null) {
            return null;
        }
        return switch (status) {
            case NOT_REVIEWED -> ContentStatus.REVIEWING;
            case REVIEWED -> ContentStatus.PUBLISHED;
            case REJECTED -> ContentStatus.REVIEW_REJECTED;
        };
    }
}
