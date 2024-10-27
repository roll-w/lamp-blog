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

package space.lingu.lamp.content.review.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import space.lingu.lamp.content.review.ReviewJob;
import space.lingu.lamp.content.review.ReviewJobInfo;
import space.lingu.lamp.content.review.ReviewerAllocator;
import space.lingu.lamp.content.review.common.ReviewException;
import space.lingu.lamp.content.review.event.OnReviewStateChangeEvent;
import space.lingu.lamp.content.review.persistence.ReviewJobDo;
import space.lingu.lamp.content.review.persistence.ReviewJobRepository;
import tech.rollw.common.web.CommonErrorCode;

import java.time.LocalDateTime;

/**
 * @author RollW
 */
@Service
public class ReviewStatusServiceImpl implements ReviewStatusService {
    private final ReviewJobRepository reviewJobRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ReviewerAllocator reviewerAllocator;

    public ReviewStatusServiceImpl(ReviewJobRepository reviewJobRepository,
                                   ApplicationEventPublisher eventPublisher,
                                   ReviewerAllocator reviewerAllocator) {
        this.reviewJobRepository = reviewJobRepository;
        this.eventPublisher = eventPublisher;
        this.reviewerAllocator = reviewerAllocator;
    }


    @Override
    public ReviewJobInfo makeReview(long jobId, long operator,
                                    boolean passed, String reason) throws ReviewException {
        ReviewJobDo job = reviewJobRepository.findById(jobId).orElse(null);
        if (job == null) {
            throw new ReviewException(CommonErrorCode.ERROR_NOT_FOUND);
        }
        // if (job.getStatus().isReviewed()) {
        //     throw new ReviewException(ReviewErrorCode.ERROR_REVIEWED);
        // }
        ReviewJobDo reviewed = switchStatus(job, operator, passed, reason);
        reviewed = reviewJobRepository.save(reviewed);

        ReviewJob reviewedJob = reviewed.lock();
        OnReviewStateChangeEvent event = new OnReviewStateChangeEvent(reviewedJob,
                job.getStatus(), reviewed.getStatus());
        eventPublisher.publishEvent(event);
        return ReviewJobInfo.of(reviewedJob);
    }


    private ReviewJobDo switchStatus(ReviewJobDo job, long operator,
                                     boolean passed, String reason) {
        if (job == null) {
            return null;
        }
        LocalDateTime time = LocalDateTime.now();
        reviewerAllocator.releaseReviewer(job.getReviewerId(), job.getReviewContentType());
        if (passed) {
            return job.reviewPass(operator, time);
        }
        return job.reviewReject(operator, reason, time);
    }
}
