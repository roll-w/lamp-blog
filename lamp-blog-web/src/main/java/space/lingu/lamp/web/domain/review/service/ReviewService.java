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
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
public interface ReviewService {
    default ReviewJob assignReviewer(Content content)
            throws NotReviewedException {
        return assignReviewer(content, false);
    }

    /**
     * Assign a reviewer to a content.
     *
     * @param allowAutoReview if false, disable {@link ReviewerAllocator#AUTO_REVIEWER
     *                        auto reviewer} and force to assign a staff reviewer.
     */
    default ReviewJob assignReviewer(Content content, boolean allowAutoReview)
            throws NotReviewedException {
        Preconditions.checkNotNull(content, "Content cannot be null");
        return assignReviewer(
                content.getContentId(),
                content.getContentType(),
                allowAutoReview
        );
    }

    ReviewJob assignReviewer(long contentId, ContentType contentType,
                             boolean allowAutoReview) throws NotReviewedException;

    List<ReviewJob> getReviewJobs(long reviewerId, Pageable pageable);

    List<ReviewJob> getReviewJobs(Pageable pageable);

    List<ReviewJob> getUnfinishedReviewJobs(long reviewerId, Pageable pageable);

    List<ReviewJob> getFinishedReviewJobs(long reviewerId, Pageable pageable);

    List<ReviewJob> getRejectedReviewJobs(long reviewerId, Pageable pageable);

    List<ReviewJob> getPassedReviewJobs(long reviewerId, Pageable pageable);

    ReviewInfo getReviewInfo(long reviewContentId, ContentType contentType);

    ReviewInfo getReviewInfo(long jobId);
}
