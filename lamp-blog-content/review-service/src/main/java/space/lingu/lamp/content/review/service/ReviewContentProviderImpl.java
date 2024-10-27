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

package space.lingu.lamp.content.review.service;

import com.google.common.base.Verify;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.content.ContentAccessService;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.review.ReviewContentProvider;
import space.lingu.lamp.content.review.ReviewJobContent;
import space.lingu.lamp.content.review.persistence.ReviewJobDo;
import space.lingu.lamp.content.review.persistence.ReviewJobRepository;

/**
 * @author RollW
 */
@Service
public class ReviewContentProviderImpl implements ReviewContentProvider {
    private final ReviewJobRepository reviewJobRepository;
    private final ContentAccessService contentAccessService;

    public ReviewContentProviderImpl(ReviewJobRepository reviewJobRepository,
                                     ContentAccessService contentAccessService) {
        this.reviewJobRepository = reviewJobRepository;
        this.contentAccessService = contentAccessService;
    }

    @NonNull
    @Override
    public ReviewJobContent getReviewContent(long jobId) {
        ReviewJobDo job = reviewJobRepository.findById(jobId).orElse(null);
        Verify.verifyNotNull(job, "Review job not found");
        ContentDetails details = contentAccessService.getContentDetails(
                job.getAssociatedContent());
        return ReviewJobContent.of(job.lock(), details);
    }
}
