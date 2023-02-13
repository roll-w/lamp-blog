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
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.common.NotReviewedException;
import space.lingu.lamp.web.domain.review.dto.ReviewInfo;

import java.util.List;

/**
 * @author RollW
 */
public interface ReviewService {
    ReviewInfo makeReview(long jobId, long operator, boolean passed, String reason);

    default ReviewJob assignReviewer(Content content)
            throws NotReviewedException {
        return assignReviewer(content, false);
    }

    /**
     * Assign a reviewer to a content.
     *
     * @param allowAutoReview if false, disable {@link ReviewerAllocateService#AUTO_REVIEWER
     *                        auto reviewer} and force to assign a staff reviewer.
     */
    default ReviewJob assignReviewer(Content content, boolean allowAutoReview)
            throws NotReviewedException {
        Preconditions.checkNotNull(content, "Reviewable cannot be null");
        return assignReviewer(
                content.getContentId(),
                content.getContentType(),
                allowAutoReview
        );
    }

    ReviewJob assignReviewer(String contentId, ContentType contentType,
                             boolean allowAutoReview) throws NotReviewedException;

    List<ReviewJob> getReviewJobs(long reviewerId, int page, int size);

    List<ReviewJob> getReviewJobs(int page, int size);

    List<ReviewJob> getUnfinishedReviewJobs(long reviewerId, int page, int size);

    List<ReviewJob> getFinishedReviewJobs(long reviewerId, int page, int size);

    List<ReviewJob> getRejectedReviewJobs(long reviewerId, int page, int size);

    List<ReviewJob> getPassedReviewJobs(long reviewerId, int page, int size);

    ReviewInfo getReviewInfo(String reviewContentId, ContentType contentType);

    ReviewInfo getReviewInfo(long jobId);
}
