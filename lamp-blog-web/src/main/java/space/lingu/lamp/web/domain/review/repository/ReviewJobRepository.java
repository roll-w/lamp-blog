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

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.ReviewJobDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class ReviewJobRepository extends AutoPrimaryBaseRepository<ReviewJob> {
    private final ReviewJobDao reviewJobDao;

    public ReviewJobRepository(LampDatabase lampDatabase,
                               ContextThreadAware<PageableContext> pageableContextThreadAware,
                               CacheManager cacheManager) {
        super(lampDatabase.getReviewJobDao(), pageableContextThreadAware, cacheManager);
        this.reviewJobDao = lampDatabase.getReviewJobDao();
    }

    @Override
    protected Class<ReviewJob> getEntityClass() {
        return ReviewJob.class;
    }

    public List<ReviewJob> getBy(ReviewStatus status) {
        return reviewJobDao.getReviewJobsByStatus(status);
    }

    public ReviewJob getBy(long contentId,
                           ContentType contentType) {
        return reviewJobDao.getReviewJobByContentId(contentId, contentType);
    }

    public List<ReviewJob> getReviewJobsByReviewer(long reviewerId) {
        return reviewJobDao.getReviewJobsByUserId(reviewerId);
    }

    public List<ReviewJob> getReviewJobsByReviewer(long reviewerId, int offset, int limit,
                                                   ReviewStatus status) {
        return reviewJobDao.getReviewJobsByStatus(reviewerId, offset, limit, status);
    }

    public List<ReviewJob> getReviewJobsByStatuses(long reviewerId, int offset, int limit,
                                                   ReviewStatus... statuses) {
        return reviewJobDao.getReviewJobsByStatuses(reviewerId, offset, limit, statuses);
    }
}