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

package space.lingu.lamp.web.controller.review;

import com.google.common.base.Verify;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.controller.WithAdminApi;
import space.lingu.lamp.web.domain.authentication.common.AuthErrorCode;
import space.lingu.lamp.web.domain.review.dto.ReviewContent;
import space.lingu.lamp.web.domain.review.dto.ReviewInfo;
import space.lingu.lamp.web.domain.review.service.ReviewContentService;
import space.lingu.lamp.web.domain.review.service.ReviewService;
import space.lingu.lamp.web.domain.review.vo.ReviewStatues;

import java.util.List;

/**
 * @author RollW
 */
@WithAdminApi
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewContentService reviewContentService;

    public ReviewController(ReviewService reviewService,
                            ReviewContentService reviewContentService) {
        this.reviewService = reviewService;
        this.reviewContentService = reviewContentService;
    }

    // TODO: verify user
    @GetMapping("/review/{jobId}")
    public HttpResponseEntity<ReviewInfo> getReviewInfo(
            @PathVariable(name = "jobId") Long jobId) {
        ReviewInfo reviewInfo = reviewService.getReviewInfo(jobId);
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();

        if (reviewInfo == null) {
            return HttpResponseEntity.of(CommonErrorCode.ERROR_NOT_FOUND, "Not found review info.");
        }
        return HttpResponseEntity.success(reviewInfo);
    }

    @GetMapping("/{userId}/reviews")
    public HttpResponseEntity<List<ReviewInfo>> getReviewInfos(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "status", required = false, defaultValue = "ALL") ReviewStatues statues) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Verify.verifyNotNull(context.userInfo());
        if (!context.allowAccess(userId)) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        List<ReviewInfo> reviewInfos = reviewService
                .getReviewJobs(userId)
                .stream()
                .map(ReviewInfo::of)
                .toList();
        return HttpResponseEntity.success(reviewInfos);
    }


    @GetMapping("/review/{jobId}/content")
    public HttpResponseEntity<ReviewContent> getReviewContent(
            @PathVariable(name = "jobId") Long jobId) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();

        ReviewContent reviewContent = reviewContentService.getReviewContent(jobId);
        return HttpResponseEntity.success(reviewContent);
    }

    // Delete: review rejected
    @DeleteMapping("/review/{jobId}")
    public HttpResponseEntity<ReviewInfo> rejectReview(
            @PathVariable(name = "jobId") Long jobId,
            @RequestBody ReviewRejectedRequest reviewRejectedRequest) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Verify.verifyNotNull(context.userInfo());
        ReviewInfo info = reviewService.makeReview(jobId, false,
                reviewRejectedRequest.reason());
        return HttpResponseEntity.success(info);
    }

    // Put: review passed
    @PutMapping("/review/{jobId}")
    public HttpResponseEntity<ReviewInfo> passReview(
            @PathVariable(name = "jobId") Long jobId) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Verify.verifyNotNull(context.userInfo());
        ReviewInfo reviewInfo = reviewService.makeReview(jobId, true, null);
        return HttpResponseEntity.success(reviewInfo);
    }
}
