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
import space.lingu.NonNull;
import space.lingu.lamp.content.ContentTrait;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJobInfo;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewJobProvider;
import space.lingu.lamp.web.domain.review.ReviewMark;
import space.lingu.lamp.web.domain.review.ReviewStatues;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.common.NotReviewedException;
import space.lingu.lamp.web.domain.review.common.ReviewException;
import space.lingu.lamp.web.domain.review.repository.ReviewJobRepository;
import tech.rollw.common.web.CommonErrorCode;
import tech.rollw.common.web.system.Operator;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ReviewServiceImpl implements ReviewService, ReviewJobProvider {
    private final ReviewJobRepository reviewJobRepository;
    private final ReviewerAllocator reviewerAllocator;

    public ReviewServiceImpl(ReviewJobRepository reviewJobRepository,
                             ReviewerAllocator reviewerAllocator) {
        this.reviewJobRepository = reviewJobRepository;
        this.reviewerAllocator = reviewerAllocator;
    }

    @Override
    public ReviewJobInfo assignReviewer(long contentId,
                                        ContentType contentType,
                                        boolean allowAutoReview) {
        long assignedTime = System.currentTimeMillis();
        ReviewJob old = reviewJobRepository.getBy(contentId, contentType);
        if (old != null && !old.getStatus().isReviewed()) {
            // if the old review job is still not reviewed, throw exception
            // we don't want to assign a new reviewer to the same content
            throw new NotReviewedException(ReviewJobInfo.of(old));
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
            // TODO:
            builder.setReviewMark(ReviewMark.REPORT);
        }

        ReviewJob reviewJob = builder
                .build();
        reviewJob = reviewJobRepository.insert(reviewJob);
        return ReviewJobInfo.of(reviewJob);
    }

    @Override
    @NonNull
    public ReviewJobInfo getReviewJob(long reviewJobId) {
        ReviewJob reviewJob = reviewJobRepository.getById(reviewJobId);
        if (reviewJob == null) {
            throw new ReviewException(CommonErrorCode.ERROR_NOT_FOUND);
        }
        return ReviewJobInfo.of(reviewJob);
    }

    // TODO: implement
    @Override
    @NonNull
    public List<ReviewJobInfo> getReviewJobs() {
        return List.of();
    }

    @Override
    @NonNull
    public List<ReviewJobInfo> getReviewJobs(long userId) {
        return List.of();
    }

    @Override
    @NonNull
    public List<ReviewJobInfo> getReviewJobs(ContentTrait contentTrait) {
        return List.of();
    }

    @Override
    @NonNull
    public List<ReviewJobInfo> getReviewJobs(ReviewStatus reviewStatus) {

        return List.of();
    }

    @Override
    @NonNull
    public List<ReviewJobInfo> getReviewJobs(ReviewStatues reviewStatues) {
        return List.of();
    }

    @NonNull
    @Override
    public List<ReviewJobInfo> getReviewJobs(
            @NonNull Operator operator,
            @NonNull ReviewStatus status) {
        return List.of();
    }

    @NonNull
    @Override
    public List<ReviewJobInfo> getReviewJobs(
            @NonNull Operator operator,
            @NonNull ReviewStatues statues) {
        return List.of();
    }
}
