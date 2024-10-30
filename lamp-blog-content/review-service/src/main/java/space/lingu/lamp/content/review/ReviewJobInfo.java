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

package space.lingu.lamp.content.review;

import space.lingu.lamp.content.ContentType;

import java.time.OffsetDateTime;

/**
 * @author RollW
 */
public record ReviewJobInfo(
        long id,
        ReviewStatus status,
        ContentType type,
        String result,
        long contentId,
        ReviewMark reviewMark,
        Long reviewer,
        Long operator,
        OffsetDateTime assignedTime,
        OffsetDateTime reviewTime
) {
    public static ReviewJobInfo of(ReviewJob job) {
        if (job == null) {
            return null;
        }
        return new ReviewJobInfo(
                job.getJobId(),
                job.getStatus(),
                job.getReviewContentType(),
                job.getResult(),
                job.getReviewContentId(),
                job.getReviewMark(),
                job.getReviewerId(),
                job.getOperatorId(),
                job.getAssignedTime(),
                job.getReviewTime()
        );
    }
}
