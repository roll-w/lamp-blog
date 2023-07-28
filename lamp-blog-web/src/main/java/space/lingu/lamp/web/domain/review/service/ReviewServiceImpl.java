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

import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewMark;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.common.NotReviewedException;
import space.lingu.lamp.web.domain.review.dto.ReviewInfo;
import space.lingu.lamp.web.domain.review.repository.ReviewJobRepository;
import tech.rollw.common.web.page.Offset;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewJobRepository reviewJobRepository;
    private final ReviewerAllocator reviewerAllocator;

    public ReviewServiceImpl(ReviewJobRepository reviewJobRepository,
                             ReviewerAllocator reviewerAllocator) {
        this.reviewJobRepository = reviewJobRepository;
        this.reviewerAllocator = reviewerAllocator;
    }

    @Override
    public ReviewJob assignReviewer(long contentId,
                                    ContentType contentType,
                                    boolean allowAutoReview) {
        long assignedTime = System.currentTimeMillis();
        ReviewJob old = reviewJobRepository.getBy(contentId, contentType);
        if (old != null && !old.getStatus().isReviewed()) {
            throw new NotReviewedException(old);
        }

        long reviewerId = reviewerAllocator.allocateReviewer(
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
    public List<ReviewJob> getReviewJobs(long reviewerId, Pageable pageable) {
        return reviewJobRepository.getReviewJobsByReviewer(reviewerId);
    }

    @Override
    public List<ReviewJob> getReviewJobs(Pageable pageable) {
        Offset offset = pageable.toOffset();
        return reviewJobRepository.get(offset);
    }

    @Override
    public List<ReviewJob> getUnfinishedReviewJobs(long reviewerId, Pageable pageable) {
        Offset offset = pageable.toOffset();
        return reviewJobRepository.getReviewJobsByReviewer(reviewerId,
                offset.offset(), offset.limit(),
                ReviewStatus.NOT_REVIEWED);
    }

    @Override
    public List<ReviewJob> getFinishedReviewJobs(long reviewerId, Pageable pageable) {
        Offset offset = pageable.toOffset();
        return reviewJobRepository.getReviewJobsByStatuses(reviewerId,
                offset.offset(), offset.limit(),
                ReviewStatus.REVIEWED,
                ReviewStatus.REJECTED);
    }

    @Override
    public List<ReviewJob> getRejectedReviewJobs(long reviewerId, Pageable pageable) {
        // TODO: get review jobs
        return null;
    }

    @Override
    public List<ReviewJob> getPassedReviewJobs(long reviewerId, Pageable pageable) {
        return null;
    }

    @Override
    public ReviewInfo getReviewInfo(long reviewContentId, ContentType contentType) {
        ReviewJob reviewJob = reviewJobRepository.getBy(reviewContentId, contentType);
        return ReviewInfo.of(reviewJob);
    }

    @Override
    public ReviewInfo getReviewInfo(long jobId) {
        return ReviewInfo.of(reviewJobRepository.getById(jobId));
    }
}
