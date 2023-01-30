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

package space.lingu.lamp.web.domain.review.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewMark;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.ReviewType;
import space.lingu.lamp.web.domain.review.Reviewable;
import space.lingu.lamp.web.domain.review.common.ReviewErrorCode;
import space.lingu.lamp.web.domain.review.common.ReviewException;
import space.lingu.lamp.web.domain.review.dto.ReviewContent;
import space.lingu.lamp.web.domain.review.dto.ReviewInfo;
import space.lingu.lamp.web.domain.review.event.OnReviewStateChangeEvent;
import space.lingu.lamp.web.domain.review.repository.ReviewJobRepository;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * @author RollW
 */
@Service
public class ReviewServiceImpl implements ReviewService, ReviewContentService {
    private final Map<ReviewType, ReviewContentServiceStrategy> reviewContentServiceStrategyMap =
            new EnumMap<>(ReviewType.class);
    private final ReviewJobRepository reviewJobRepository;
    private final ReviewerAllocateService reviewerAllocateService;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewServiceImpl(List<ReviewContentServiceStrategy> contentServiceStrategies,
                             ReviewJobRepository reviewJobRepository,
                             ReviewerAllocateService reviewerAllocateService,
                             ApplicationEventPublisher eventPublisher) {
        this.reviewJobRepository = reviewJobRepository;
        this.reviewerAllocateService = reviewerAllocateService;
        this.eventPublisher = eventPublisher;
        contentServiceStrategies.forEach(strategy ->
                reviewContentServiceStrategyMap.put(strategy.getReviewType(), strategy));
    }

    @Override
    public ReviewInfo makeReview(long jobId, boolean passed, String reason) {
        ReviewJob job = reviewJobRepository.get(jobId);
        Verify.verifyNotNull(job, "Review job not found");
        if (job.getStatus().isReviewed()) {
            throw new ReviewException(ReviewErrorCode.ERROR_REVIEWED);
        }
        ReviewJob reviewed = switchStatus(job, passed, reason);
        OnReviewStateChangeEvent event = new OnReviewStateChangeEvent(reviewed,
                job.getStatus(), reviewed.getStatus());
        eventPublisher.publishEvent(event);
        return ReviewInfo.of(job);
    }

    private ReviewJob switchStatus(ReviewJob job, boolean passed, String reason) {
        if (job == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        if (passed) {
            return job.reviewPass(time);
        }
        return job.reviewReject(reason, time);
    }

    @Override
    public ReviewJob assignReviewer(Reviewable reviewable) {
        Preconditions.checkNotNull(reviewable, "Reviewable cannot be null");
        return assignReviewer(reviewable.getReviewContentId(), reviewable.getReviewType());
    }

    @Override
    public ReviewJob assignReviewer(String contentId, ReviewType type) {
        long assignedTime = System.currentTimeMillis();
        long reviewerId = reviewerAllocateService.allocateReviewer(type);

        ReviewJob reviewJob = ReviewJob.builder()
                .setReviewContentId(contentId)
                .setReviewerId(reviewerId)
                .setType(type)
                .setStatus(ReviewStatus.NOT_REVIEWED)
                .setAssignedTime(assignedTime)
                .setReviewMark(ReviewMark.NORMAL)
                // TODO: distinguish between different mark types of review
                // if first time review, mark it as normal
                // if it is the second or later review, mark it as report
                .build();
        reviewJob = reviewJobRepository.insert(reviewJob);
        return reviewJob;
    }

    @Override
    public List<ReviewJob> getReviewJobs(long reviewerId) {
        return reviewJobRepository.getReviewJobsByReviewer(reviewerId);
    }

    @Override
    public List<ReviewJob> getUnfinishedReviewJobs(long reviewerId) {
        return reviewJobRepository.getReviewJobsByReviewer(reviewerId, ReviewStatus.NOT_REVIEWED);
    }

    @Override
    public List<ReviewJob> getFinishedReviewJobs(long reviewerId) {
        return reviewJobRepository.getReviewJobsByStatuses(reviewerId,
                ReviewStatus.REVIEWED, ReviewStatus.REJECTED);
    }

    @Override
    public ReviewInfo getReviewInfo(String reviewContentId, ReviewType type) {
        ReviewJob reviewJob = reviewJobRepository.getBy(reviewContentId, type);
        return ReviewInfo.of(reviewJob);
    }

    @Override
    public ReviewInfo getReviewInfo(long jobId) {
        return ReviewInfo.of(reviewJobRepository.get(jobId));
    }

    @NonNull
    @Override
    public ReviewContent getReviewContent(long jobId) {
        ReviewJob job = reviewJobRepository.get(jobId);
        Verify.verifyNotNull(job, "Review job not found");
        ReviewContentServiceStrategy strategy =
                reviewContentServiceStrategyMap.get(job.getType());
        return strategy.getContent(job.getReviewContentId());
    }
}
