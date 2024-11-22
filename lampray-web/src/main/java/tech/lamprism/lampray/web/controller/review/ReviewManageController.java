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

package tech.lamprism.lampray.web.controller.review;

import org.springframework.web.bind.annotation.GetMapping;
import tech.lamprism.lampray.web.controller.AdminApi;
import tech.lamprism.lampray.content.review.ReviewJobProvider;
import tech.lamprism.lampray.content.review.ReviewJobInfo;
import tech.rollw.common.web.HttpResponseEntity;

import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class ReviewManageController {
    private final ReviewJobProvider reviewJobProvider;

    public ReviewManageController(ReviewJobProvider reviewJobProvider) {
        this.reviewJobProvider = reviewJobProvider;
    }

    @GetMapping("/reviews")
    public HttpResponseEntity<List<ReviewJobInfo>> getReviewInfos() {
        List<ReviewJobInfo> reviewJobInfos = reviewJobProvider.getReviewJobs();
        return HttpResponseEntity.success(reviewJobInfos);
    }
}
