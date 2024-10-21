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

package space.lingu.lamp.web.database.dao;

import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.light.Dao;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface ReviewJobDao extends AutoPrimaryBaseDao<ReviewJob> {
    @Override
    @Query("SELECT * FROM review_job WHERE id = {id}")
    ReviewJob getById(long id);

    @Override
    @Query("SELECT * FROM review_job WHERE id IN ({ids})")
    List<ReviewJob> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM review_job ORDER BY id DESC")
    List<ReviewJob> get();

    @Override
    @Query("SELECT COUNT(*) FROM review_job")
    int count();

    @Override
    @Query("SELECT * FROM review_job ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<ReviewJob> get(Offset offset);

    @Override
    default String getTableName() {
        return "review_job";
    }

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId}")
    List<ReviewJob> getReviewJobsByUserId(long userId);

    @Query("SELECT * FROM review_job WHERE status = {status}")
    List<ReviewJob> getReviewJobsByStatus(ReviewStatus status);

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId} AND `status` = {status} LIMIT {limit} OFFSET {offset}")
    List<ReviewJob> getReviewJobsByStatus(long userId, int offset, int limit, ReviewStatus status);

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId} AND `status` IN ({statuses})")
    List<ReviewJob> getReviewJobsByStatus(long userId, ReviewStatus... statuses);

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId} AND `status` IN ({statuses}) LIMIT {limit} OFFSET {offset}")
    List<ReviewJob> getReviewJobsByStatuses(long userId, int offset, int limit, ReviewStatus... statuses);

    @Query("SELECT * FROM review_job WHERE content_id = {contentId} AND type = {type}")
    ReviewJob getReviewJobByContentId(long contentId, ContentType type);
}
