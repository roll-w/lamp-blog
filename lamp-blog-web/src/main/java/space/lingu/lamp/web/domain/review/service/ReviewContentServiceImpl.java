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

import com.google.common.base.Verify;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.content.ContentAccessService;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewJobContent;
import space.lingu.lamp.web.domain.review.repository.ReviewJobRepository;

/**
 * @author RollW
 */
@Service
public class ReviewContentServiceImpl implements ReviewContentService {
    private final ReviewJobRepository reviewJobRepository;
    private final ContentAccessService contentAccessService;

    public ReviewContentServiceImpl(ReviewJobRepository reviewJobRepository,
                                    ContentAccessService contentAccessService) {
        this.reviewJobRepository = reviewJobRepository;
        this.contentAccessService = contentAccessService;
    }


    @NonNull
    @Override
    public ReviewJobContent getReviewContent(long jobId) {
        ReviewJob job = reviewJobRepository.getById(jobId);
        Verify.verifyNotNull(job, "Review job not found");
        ContentDetails details = contentAccessService.getContentDetails(
                job.getAssociatedContent());
        return ReviewJobContent.of(job, details);
    }
}
