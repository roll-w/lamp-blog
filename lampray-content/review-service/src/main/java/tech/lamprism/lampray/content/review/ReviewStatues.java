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

package tech.lamprism.lampray.content.review;

/**
 * @author RollW
 */
public enum ReviewStatues {
    FINISHED(ReviewStatus.REVIEWED, ReviewStatus.REJECTED),
    UNFINISHED(ReviewStatus.NOT_REVIEWED),
    PASSED(ReviewStatus.REVIEWED),
    REJECTED(ReviewStatus.REJECTED),
    ALL(ReviewStatus.NOT_REVIEWED, ReviewStatus.REVIEWED, ReviewStatus.REJECTED);

    private final ReviewStatus[] statuses;

    ReviewStatues(ReviewStatus... statuses) {
        this.statuses = statuses;
    }

    public ReviewStatus[] getStatuses() {
        return statuses;
    }
}
