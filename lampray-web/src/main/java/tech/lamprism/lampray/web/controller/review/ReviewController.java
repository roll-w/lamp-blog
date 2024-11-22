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

import com.google.common.base.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.lamprism.lampray.LampException;
import tech.lamprism.lampray.web.common.ApiContext;
import tech.lamprism.lampray.web.controller.Api;
import tech.lamprism.lampray.web.controller.review.model.ReviewRequest;
import tech.lamprism.lampray.content.review.ReviewJobContent;
import tech.lamprism.lampray.content.review.ReviewJobInfo;
import tech.lamprism.lampray.content.review.ReviewJobProvider;
import tech.lamprism.lampray.content.review.ReviewStatues;
import tech.lamprism.lampray.content.review.ReviewContentProvider;
import tech.lamprism.lampray.content.review.service.ReviewStatusService;
import tech.lamprism.lampray.user.UserIdentity;
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
    private final ReviewContentProvider reviewContentProvider;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;


    public ReviewController(ReviewJobProvider reviewJobProvider,
                            ReviewStatusService reviewStatusService,
                            ReviewContentProvider reviewContentProvider,
                            ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.reviewJobProvider = reviewJobProvider;
        this.reviewStatusService = reviewStatusService;
        this.reviewContentProvider = reviewContentProvider;
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @GetMapping("/reviews/{jobId}")
    public HttpResponseEntity<ReviewJobInfo> getReviewInfo(
            @PathVariable(name = "jobId") Long jobId) {
        ReviewJobInfo reviewJobInfo = reviewJobProvider.getReviewJob(jobId);
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());
        if (reviewJobInfo.reviewer() != user.getOperatorId()) {
            throw new LampException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        return HttpResponseEntity.success(reviewJobInfo);
    }

    /**
     * Get current user's review infos.
     */
    @GetMapping({"/reviews"})
    public HttpResponseEntity<List<ReviewJobInfo>> getReviewInfo(
            @RequestParam(name = "status", required = false,
                    defaultValue = "ALL")
            ReviewStatues statues) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());

        List<ReviewJobInfo> reviewJobInfos = reviewJobProvider
                .getReviewJobs(user, statues);
        return HttpResponseEntity.success(reviewJobInfos);
    }

    @GetMapping("/users/{userId}/reviews")
    public HttpResponseEntity<List<ReviewJobInfo>> getReviewInfos(
            @PathVariable(name = "userId") Long userId,
            @RequestParam(name = "status", required = false,
                    defaultValue = "ALL")
            ReviewStatues statues) {
        // TODO: impl
        List<ReviewJobInfo> reviewJobInfos = List.of();
        return HttpResponseEntity.success(reviewJobInfos);
    }

    @GetMapping("/reviews/{jobId}/content")
    public HttpResponseEntity<ReviewJobContent> getReviewContent(
            @PathVariable(name = "jobId") Long jobId) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());
        ReviewJobContent reviewJobContent = reviewContentProvider
                .getReviewContent(jobId);
        ReviewJobInfo reviewJobInfo = reviewJobContent.getReviewJobInfo();
        if (reviewJobInfo.reviewer() != user.getOperatorId()) {
            throw new LampException(AuthErrorCode.ERROR_NOT_HAS_ROLE);
        }
        return HttpResponseEntity.success(reviewJobContent);
    }

    @PostMapping("/reviews/{jobId}")
    public HttpResponseEntity<ReviewJobInfo> makeReview(
            @PathVariable(name = "jobId") Long jobId,
            @RequestBody ReviewRequest reviewRequest
    ) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        UserIdentity user = Verify.verifyNotNull(apiContext.getUser());
        ReviewJobInfo reviewJobInfo = reviewStatusService.makeReview(
                jobId,
                user.getOperatorId(),
                reviewRequest.getPass(),
                reviewRequest.getResult()
        );
        return HttpResponseEntity.success(reviewJobInfo);
    }
}
