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

import com.google.common.base.Preconditions;
import space.lingu.lamp.content.Content;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.review.ReviewJobInfo;
import space.lingu.lamp.content.review.ReviewerAllocator;
import space.lingu.lamp.content.review.common.NotReviewedException;

/**
 * @author RollW
 */
public interface ReviewService {
    // TODO: needs rename

    default ReviewJobInfo assignReviewer(Content content)
            throws NotReviewedException {
        return assignReviewer(content, false);
    }

    /**
     * Assign a reviewer to a content.
     *
     * @param allowAutoReview if false, disable {@link ReviewerAllocator#AUTO_REVIEWER
     *                        auto reviewer} and force to assign a staff reviewer.
     */
    default ReviewJobInfo assignReviewer(Content content, boolean allowAutoReview)
            throws NotReviewedException {
        Preconditions.checkNotNull(content, "Content cannot be null");
        return assignReviewer(
                content.getContentId(),
                content.getContentType(),
                allowAutoReview
        );
    }


    ReviewJobInfo assignReviewer(long contentId, ContentType contentType,
                                 boolean allowAutoReview) throws NotReviewedException;

}
