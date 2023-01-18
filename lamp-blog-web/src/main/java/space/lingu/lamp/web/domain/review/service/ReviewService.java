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

import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.dto.ReviewResult;
import space.lingu.lamp.web.domain.review.ReviewType;
import space.lingu.lamp.web.domain.review.Reviewable;

import java.util.List;

/**
 * @author RollW
 */
public interface ReviewService {
    ReviewResult makeReview(long jobId, boolean passed, String reason);

    ReviewJob assignReviewer(Reviewable reviewable);

    ReviewJob assignReviewer(String contentId, ReviewType type);

    List<ReviewJob> getReviewJobs(long reviewerId);

    List<ReviewJob> getUnfinishedReviewJobs(long reviewerId);

    List<ReviewJob> getFinishedReviewJobs(long reviewerId);

    ReviewResult getReviewResult(long reviewerId, String reviewContentId, ReviewType type);
}
