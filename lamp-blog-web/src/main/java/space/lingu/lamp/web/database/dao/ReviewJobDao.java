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

import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.review.ReviewJob;
import space.lingu.lamp.web.domain.review.ReviewStatus;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;
import space.lingu.light.Update;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class ReviewJobDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(ReviewJob... reviewJobs);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insert(ReviewJob reviewJob);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(List<ReviewJob> reviewJobs);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(ReviewJob... reviewJobs);

    @Delete
    public abstract void delete(ReviewJob ReviewJob);

    @Delete
    public abstract void delete(List<ReviewJob> reviewJobs);

    @Delete("DELETE FROM review_job")
    public abstract void clearTable();

    @Query("SELECT * FROM review_job")
    public abstract List<ReviewJob> get();

    @Query("SELECT * FROM review_job WHERE id = {id}")
    public abstract ReviewJob get(long id);

    @Query("SELECT * FROM review_job ORDER BY id LIMIT {limit} OFFSET {offset}")
    public abstract List<ReviewJob> getByPage(int offset, int limit);

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId}")
    public abstract List<ReviewJob> getReviewJobsByUserId(long userId);

    @Query("SELECT * FROM review_job WHERE status = {status}")
    public abstract List<ReviewJob> getReviewJobsByStatus(ReviewStatus status);

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId} AND `status` = {status} LIMIT {limit} OFFSET {offset}")
    public abstract List<ReviewJob> getReviewJobsByStatus(long userId, int offset, int limit, ReviewStatus status);

    @Query("SELECT * FROM review_job WHERE reviewer_id = {userId} AND `status` IN ({statuses}) LIMIT {limit} OFFSET {offset}")
    public abstract List<ReviewJob> getReviewJobsByStatuses(long userId, int offset, int limit,  ReviewStatus... statuses);

    @Query("SELECT * FROM review_job WHERE content_id = {contentId} AND type = {type}")
    public abstract ReviewJob getReviewJobByContentId(String contentId, ContentType type);
}

