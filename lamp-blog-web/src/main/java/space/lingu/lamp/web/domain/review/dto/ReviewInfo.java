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

package space.lingu.lamp.web.domain.review.dto;

import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.ReviewType;

/**
 * @author RollW
 */
public record ReviewInfo(
        ReviewStatus status,
        ReviewType type,
        String result,
        String contentId,
        long reviewer,
        long reviewTime
) {
    public static ReviewInfo of(ReviewJob job) {
        if (job == null) {
            return null;
        }
        return new ReviewInfo(
                job.getStatus(),
                job.getType(),
                job.getResult(),
                job.getReviewContentId(),
                job.getReviewerId(),
                job.getReviewTime()
        );
    }
}
