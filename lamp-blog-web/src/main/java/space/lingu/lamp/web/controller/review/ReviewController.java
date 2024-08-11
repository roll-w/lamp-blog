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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import space.lingu.lamp.LampException;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.controller.Api;
import space.lingu.lamp.web.controller.review.model.ReviewRequest;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewJobProvider;
import space.lingu.lamp.web.domain.review.ReviewStatues;
import space.lingu.lamp.web.domain.review.ReviewContent;
import space.lingu.lamp.web.domain.review.ReviewInfo;
import space.lingu.lamp.web.domain.review.service.ReviewContentService;
import space.lingu.lamp.web.domain.review.service.ReviewStatusService;
import space.lingu.lamp.web.domain.user.UserIdentity;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;

import java.util.List;

/**
 * @author RollW
 */
@Api
public class ReviewController {
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    private final ReviewJobProvider reviewJobProvider;
    private final ReviewStatusService reviewStatusService;
    private final ReviewContentService reviewContentService;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;


    public ReviewController(ReviewJobProvider reviewJobProvider,
                            ReviewStatusService reviewStatusService,
                            ReviewContentService reviewContentService,
                            ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.reviewJobProvider = reviewJobProvider;
        this.reviewStatusService = reviewStatusService;
        this.reviewContentService = reviewContentService;
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @GetMapping("/reviews/{jobId}")
    public HttpResponseEntity<ReviewInfo> getReviewInfo(
            @PathVariable(name = "jobId") Long jobId) {
        ReviewJob reviewJob = reviewJobProvider.getReviewJob(jobId);
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());
        if (reviewJob.getReviewerId() != user.getOperatorId()) {
            throw new LampException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        return HttpResponseEntity.success(ReviewInfo.of(reviewJob));
    }

    /**
     * Get current user's review infos.
     */
    @GetMapping("/reviews")
    public HttpResponseEntity<List<ReviewInfo>> getReviewInfo(
            @RequestParam(name = "status", required = false,
                    defaultValue = "ALL")
            ReviewStatues statues) {

        List<ReviewInfo> reviewInfos = List.of();
        return HttpResponseEntity.success(reviewInfos);
    }

    @GetMapping("/users/{userId}/reviews")
    public HttpResponseEntity<List<ReviewInfo>> getReviewInfos(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "status", required = false,
                    defaultValue = "ALL")
            ReviewStatues statues) {
        // TODO: impl
        List<ReviewInfo> reviewInfos = List.of();
        return HttpResponseEntity.success(reviewInfos);
    }

    @GetMapping("/reviews/{jobId}/content")
    public HttpResponseEntity<ReviewContent> getReviewContent(
            @PathVariable(name = "jobId") Long jobId) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());
        ReviewContent reviewContent = reviewContentService
                .getReviewContent(jobId);
        ReviewInfo reviewInfo = reviewContent.getReviewInfo();
        if (reviewInfo.reviewer() != user.getOperatorId()) {
            throw new LampException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        return HttpResponseEntity.success(reviewContent);
    }

    @PostMapping("/reviews/{jobId}")
    public HttpResponseEntity<ReviewInfo> makeReview(
            @PathVariable(name = "jobId") Long jobId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());
        ReviewInfo reviewInfo = reviewStatusService.makeReview(
                jobId,
                user.getOperatorId(),
                reviewRequest.getPass(),
                reviewRequest.getResult()
        );
        return HttpResponseEntity.success(reviewInfo);
    }
}
