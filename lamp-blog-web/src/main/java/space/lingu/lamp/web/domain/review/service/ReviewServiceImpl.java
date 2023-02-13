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

import com.google.common.base.Verify;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import space.lingu.lamp.data.page.Offset;
import space.lingu.lamp.data.page.PageHelper;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewMark;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.common.NotReviewedException;
import space.lingu.lamp.web.domain.review.common.ReviewErrorCode;
import space.lingu.lamp.web.domain.review.common.ReviewException;
import space.lingu.lamp.web.domain.review.dto.ReviewInfo;
import space.lingu.lamp.web.domain.review.event.OnReviewStateChangeEvent;
import space.lingu.lamp.web.domain.review.repository.ReviewJobRepository;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewJobRepository reviewJobRepository;
    private final ReviewerAllocateService reviewerAllocateService;
    private final ApplicationEventPublisher eventPublisher;

    public ReviewServiceImpl(ReviewJobRepository reviewJobRepository,
                             ReviewerAllocateService reviewerAllocateService,
                             ApplicationEventPublisher eventPublisher) {
        this.reviewJobRepository = reviewJobRepository;
        this.reviewerAllocateService = reviewerAllocateService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ReviewInfo makeReview(long jobId, long operator,
                                 boolean passed, String reason) {
        ReviewJob job = reviewJobRepository.get(jobId);
        Verify.verifyNotNull(job, "Review job not found");
        if (job.getStatus().isReviewed()) {
            throw new ReviewException(ReviewErrorCode.ERROR_REVIEWED);
        }

        ReviewJob reviewed = switchStatus(job, operator, passed, reason);
        reviewJobRepository.update(reviewed);

        OnReviewStateChangeEvent event = new OnReviewStateChangeEvent(reviewed,
                job.getStatus(), reviewed.getStatus());
        eventPublisher.publishEvent(event);
        return ReviewInfo.of(job);
    }

    private ReviewJob switchStatus(ReviewJob job, long operator,
                                   boolean passed, String reason) {
        if (job == null) {
            return null;
        }
        long time = System.currentTimeMillis();
        reviewerAllocateService.releaseReviewer(job.getReviewerId(), job.getType());
        if (passed) {
            return job.reviewPass(operator, time);
        }
        return job.reviewReject(operator, reason, time);
    }

    @Override
    public ReviewJob assignReviewer(String contentId, ContentType contentType,
                                    boolean allowAutoReview) {
        long assignedTime = System.currentTimeMillis();
        ReviewJob old = reviewJobRepository.getBy(contentId, contentType);
        if (old != null && !old.getStatus().isReviewed()) {
            throw new NotReviewedException(old);
        }

        long reviewerId = reviewerAllocateService.allocateReviewer(
                contentType, allowAutoReview);

        ReviewJob.Builder builder = ReviewJob.builder()
                .setReviewContentId(contentId)
                .setReviewerId(reviewerId)
                .setType(contentType)
                .setStatus(ReviewStatus.NOT_REVIEWED)
                .setAssignedTime(assignedTime)
                .setReviewMark(ReviewMark.NORMAL);
        if (old != null) {
            // if first time review, mark it as normal
            // if it is the second or later review, mark it as report
            builder.setReviewMark(ReviewMark.REPORT);
        }

        ReviewJob reviewJob = builder
                .build();
        reviewJob = reviewJobRepository.insert(reviewJob);
        return reviewJob;
    }

    @Override
    public List<ReviewJob> getReviewJobs(long reviewerId, int page, int size) {
        return reviewJobRepository.getReviewJobsByReviewer(reviewerId);
    }

    @Override
    public List<ReviewJob> getReviewJobs(int page, int size) {
        Offset offset = PageHelper.offset(page, size);
        return reviewJobRepository.getAll(offset.offset(), offset.limit());
    }

    @Override
    public List<ReviewJob> getUnfinishedReviewJobs(long reviewerId, int page, int size) {
        Offset offset = PageHelper.offset(page, size);
        return reviewJobRepository.getReviewJobsByReviewer(reviewerId,
                offset.offset(), offset.limit(),
                ReviewStatus.NOT_REVIEWED);
    }

    @Override
    public List<ReviewJob> getFinishedReviewJobs(long reviewerId, int page, int size) {
        Offset offset = PageHelper.offset(page, size);
        return reviewJobRepository.getReviewJobsByStatuses(reviewerId,
                offset.offset(), offset.limit(),
                ReviewStatus.REVIEWED,
                ReviewStatus.REJECTED);
    }

    @Override
    public List<ReviewJob> getRejectedReviewJobs(long reviewerId, int page, int size) {
        // TODO: get review jobs
        return null;
    }

    @Override
    public List<ReviewJob> getPassedReviewJobs(long reviewerId, int page, int size) {
        return null;
    }

    @Override
    public ReviewInfo getReviewInfo(String reviewContentId, ContentType contentType) {
        ReviewJob reviewJob = reviewJobRepository.getBy(reviewContentId, contentType);
        return ReviewInfo.of(reviewJob);
    }

    @Override
    public ReviewInfo getReviewInfo(long jobId) {
        return ReviewInfo.of(reviewJobRepository.get(jobId));
    }
}
