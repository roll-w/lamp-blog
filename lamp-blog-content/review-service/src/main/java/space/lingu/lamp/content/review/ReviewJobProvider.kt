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
package space.lingu.lamp.content.review

import space.lingu.lamp.content.ContentTrait
import tech.rollw.common.web.system.Operator

/**
 * @author RollW
 */
interface ReviewJobProvider {
    fun getReviewJob(reviewJobId: Long): ReviewJobInfo

    val reviewJobs: List<ReviewJobInfo>

    fun getReviewJobs(operatorId: Long): List<ReviewJobInfo>

    fun getReviewJobs(
        reviewer: Operator,
        status: ReviewStatus
    ): List<ReviewJobInfo>

    fun getReviewJobs(
        reviewer: Operator,
        statues: ReviewStatues = ReviewStatues.ALL
    ): List<ReviewJobInfo>

    fun getReviewJobs(contentTrait: ContentTrait): List<ReviewJobInfo>

    fun getReviewJobs(reviewStatus: ReviewStatus): List<ReviewJobInfo>

    fun getReviewJobs(reviewStatues: ReviewStatues): List<ReviewJobInfo>
}
