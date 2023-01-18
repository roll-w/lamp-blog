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

package space.lingu.lamp.web.domain.review.repository;

import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.ReviewJobDao;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.lamp.web.domain.review.ReviewType;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class ReviewJobRepository {
    private final ReviewJobDao reviewJobDao;

    public ReviewJobRepository(LampDatabase database) {
        reviewJobDao = database.getReviewJobDao();
    }

    public ReviewJob insert(ReviewJob reviewJob) {
        long id = reviewJobDao.insert(reviewJob);
        return reviewJob.fork(id);
    }


    public void update(ReviewJob reviewJob) {
        reviewJobDao.update(reviewJob);
    }

    public ReviewJob get(long id) {
        return reviewJobDao.get(id);
    }

    public ReviewJob getBy(String contentId, ReviewType reviewType) {
        return reviewJobDao.getReviewJobByContentId(contentId, reviewType);
    }

    public List<ReviewJob> getReviewJobsByReviewer(long reviewerId) {
        return reviewJobDao.getReviewJobsByUserId(reviewerId);
    }

    public List<ReviewJob> getReviewJobsByReviewer(long reviewerId, ReviewStatus status) {
        return reviewJobDao.getReviewJobsByStatus(reviewerId, status);
    }

    public List<ReviewJob> getReviewJobsByStatuses(long reviewerId, ReviewStatus... statuses) {
        return reviewJobDao.getReviewJobsByStatuses(reviewerId, statuses);
    }
}
