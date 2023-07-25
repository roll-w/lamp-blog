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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.controller.WithAdminApi;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.dto.ReviewContent;
import space.lingu.lamp.web.domain.review.dto.ReviewInfo;
import space.lingu.lamp.web.domain.review.service.ReviewContentService;
import space.lingu.lamp.web.domain.review.service.ReviewService;
import space.lingu.lamp.web.domain.review.service.ReviewStatusService;
import space.lingu.lamp.web.domain.review.vo.ReviewStatues;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.CommonErrorCode;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
@WithAdminApi
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewService reviewService;
    private final ReviewStatusService reviewStatusService;
    private final ReviewContentService reviewContentService;

    public ReviewController(ReviewService reviewService,
                            ReviewStatusService reviewStatusService,
                            ReviewContentService reviewContentService) {
        this.reviewService = reviewService;
        this.reviewStatusService = reviewStatusService;
        this.reviewContentService = reviewContentService;
    }

    // TODO: verify user
    @GetMapping("/review/{jobId}")
    public HttpResponseEntity<ReviewInfo> getReviewInfo(
            @PathVariable(name = "jobId") Long jobId) {
        ReviewInfo reviewInfo = reviewService.getReviewInfo(jobId);
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        if (reviewInfo == null) {
            return HttpResponseEntity.of(CommonErrorCode.ERROR_NOT_FOUND,
                    "Not found review job.");
        }
        if (apiContext.allowAccessResource(reviewInfo.reviewer())) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        return HttpResponseEntity.success(reviewInfo);
    }

    @GetMapping("/reviews")
    public HttpResponseEntity<List<ReviewInfo>> getReviewInfo(
            @RequestParam(name = "status", required = false,
                    defaultValue = "ALL")
            ReviewStatues statues,
            Pageable pageable) {
        if (ApiContextHolder.getContext().isAdminPass()) {
            return adminGetReviewInfos(pageable);
        }
        List<ReviewInfo> reviewInfos = getReviewJobs(
                ApiContextHolder.getContext().id(),
                pageable,
                statues
        ).stream()
                .map(ReviewInfo::of)
                .toList();
        return HttpResponseEntity.success(reviewInfos);
    }

    private HttpResponseEntity<List<ReviewInfo>> adminGetReviewInfos(
            Pageable pageable) {
        List<ReviewInfo> reviewInfos = reviewService.getReviewJobs(pageable)
                .stream()
                .map(ReviewInfo::of)
                .toList();
        return HttpResponseEntity.success(reviewInfos);
    }

    @GetMapping("/user/{userId}/reviews")
    public HttpResponseEntity<List<ReviewInfo>> getReviewInfos(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "status", required = false,
                    defaultValue = "ALL")
            ReviewStatues statues,
            Pageable pageable) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Verify.verifyNotNull(context.userInfo());
        if (!context.allowAccessResource(userId)) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }

        List<ReviewInfo> reviewInfos = getReviewJobs(userId,
                pageable,
                statues)
                .stream()
                .map(ReviewInfo::of)
                .toList();
        return HttpResponseEntity.success(reviewInfos);
    }

    private List<ReviewJob> getReviewJobs(Long userId,
                                          Pageable pageable,
                                          ReviewStatues statues) {
        return switch (statues) {
            case FINISHED -> reviewService.getFinishedReviewJobs(userId, pageable);
            case UNFINISHED -> reviewService.getUnfinishedReviewJobs(userId, pageable);
            case PASSED -> reviewService.getPassedReviewJobs(userId, pageable);
            case REJECTED -> reviewService.getRejectedReviewJobs(userId, pageable);
            case ALL -> reviewService.getReviewJobs(userId, pageable);
        };
    }

    @GetMapping("/review/{jobId}/content")
    public HttpResponseEntity<ReviewContent> getReviewContent(
            @PathVariable(name = "jobId") Long jobId) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        if (context.isAdminPass()) {
            ReviewContent reviewContent = reviewContentService.getReviewContent(jobId);
            return HttpResponseEntity.success(reviewContent);
        }
        ReviewInfo reviewInfo = reviewService.getReviewInfo(jobId);
        if (!context.allowAccessResource(reviewInfo.reviewer())) {
            return HttpResponseEntity.of(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        ReviewContent reviewContent = reviewContentService.getReviewContent(jobId);
        return HttpResponseEntity.success(reviewContent);
    }


    @PatchMapping(value = "/review/{jobId}")
    // should be use DELETE method, but DELETE method can't have body
    // so use PATCH method instead
    public HttpResponseEntity<ReviewInfo> rejectReview(
            @PathVariable(name = "jobId") Long jobId,
            @RequestBody ReviewRejectedRequest reviewRejectedRequest) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Verify.verifyNotNull(context.userInfo());
        ReviewInfo info = reviewStatusService.makeReview(jobId, context.id(),
                false, reviewRejectedRequest.reason());
        return HttpResponseEntity.success(info);
    }

    // Put: review passed
    @PutMapping("/review/{jobId}")
    public HttpResponseEntity<ReviewInfo> passReview(
            @PathVariable(name = "jobId") Long jobId) {
        ApiContextHolder.ApiContext context = ApiContextHolder.getContext();
        Verify.verifyNotNull(context.userInfo());
        ReviewInfo reviewInfo = reviewStatusService.makeReview(jobId,
                context.id(), true, null);
        return HttpResponseEntity.success(reviewInfo);
    }
}
