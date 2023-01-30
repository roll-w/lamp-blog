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

package space.lingu.lamp.web.domain.review.event;

import org.springframework.context.ApplicationEvent;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;

/**
 * @author RollW
 */
public class OnReviewStateChangeEvent extends ApplicationEvent {
    private final ReviewJob reviewJob;
    private final ReviewStatus previousStatus;
    private final ReviewStatus currentStatus;

    public OnReviewStateChangeEvent(@NonNull ReviewJob reviewJob,
                                    @NonNull ReviewStatus previousStatus,
                                    @NonNull ReviewStatus currentStatus) {
        super(reviewJob);
        this.reviewJob = reviewJob;
        this.previousStatus = previousStatus;
        this.currentStatus = currentStatus;
    }

    public ReviewJob getReviewJob() {
        return reviewJob;
    }

    public ReviewStatus getPreviousStatus() {
        return previousStatus;
    }

    public ReviewStatus getCurrentStatus() {
        return currentStatus;
    }
}
