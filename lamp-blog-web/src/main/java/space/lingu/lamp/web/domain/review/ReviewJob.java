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

package space.lingu.lamp.web.domain.review;

import space.lingu.lamp.DataItem;
import space.lingu.light.Constructor;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(tableName = "review_job")
public class ReviewJob implements DataItem {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long jobId;

    // TODO: may add serial number to replace job id in the future
    // private String serialNumber;

    @DataColumn(name = "content_id")
    private final String reviewContentId;

    @DataColumn(name = "reviewer_id")
    private final long reviewerId;

    @DataColumn(name = "operator_id")
    private final long operatorId;

    @DataColumn(name = "status")
    private final ReviewStatus status;

    @DataColumn(name = "type")
    private final ReviewType type;

    @DataColumn(name = "create_time")
    private final long assignedTime;

    @DataColumn(name = "result")
    private final String result;

    @DataColumn(name = "review_time")
    private final long reviewTime;

    @Constructor
    public ReviewJob(Long jobId, String reviewContentId, long reviewerId,
                     long operatorId, ReviewStatus status, ReviewType type,
                     long assignedTime, String result, long reviewTime) {
        this.jobId = jobId;
        this.reviewContentId = reviewContentId;
        this.reviewerId = reviewerId;
        this.operatorId = operatorId;
        this.status = status;
        this.type = type;
        this.assignedTime = assignedTime;
        this.result = result;
        this.reviewTime = reviewTime;
    }

    public ReviewJob(String reviewContentId, long reviewerId,
                     long operatorId, ReviewStatus status, ReviewType type,
                     long assignedTime, String result, long reviewTime) {
        this.operatorId = operatorId;
        this.jobId = null;
        this.reviewContentId = reviewContentId;
        this.reviewerId = reviewerId;
        this.status = status;
        this.type = type;
        this.assignedTime = assignedTime;
        this.result = result;
        this.reviewTime = reviewTime;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getReviewContentId() {
        return reviewContentId;
    }

    public long getReviewerId() {
        return reviewerId;
    }

    public long getOperatorId() {
        return operatorId;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public ReviewType getType() {
        return type;
    }

    public long getAssignedTime() {
        return assignedTime;
    }

    public String getResult() {
        return result;
    }

    public long getReviewTime() {
        return reviewTime;
    }

    public ReviewJob reviewPass(long reviewTime) {
        return new ReviewJob(
                jobId, reviewContentId, reviewerId,
                operatorId, ReviewStatus.REVIEWED,
                type, assignedTime, null, reviewTime
        );
    }

    public ReviewJob reviewReject(String result, long reviewTime) {
        return new ReviewJob(
                jobId, reviewContentId, reviewerId,
                operatorId, ReviewStatus.REJECTED,
                type, assignedTime, result, reviewTime
        );
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewJob job = (ReviewJob) o;
        return reviewerId == job.reviewerId && operatorId == job.operatorId && assignedTime == job.assignedTime && reviewTime == job.reviewTime && Objects.equals(jobId, job.jobId) && Objects.equals(reviewContentId, job.reviewContentId) && status == job.status && type == job.type && Objects.equals(result, job.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, reviewContentId, reviewerId, operatorId, status, type, assignedTime, result, reviewTime);
    }

    @Override
    public String toString() {
        return "ReviewJob{" +
                "jobId=" + jobId +
                ", reviewContentId='" + reviewContentId + '\'' +
                ", reviewerId=" + reviewerId +
                ", operatorId=" + operatorId +
                ", status=" + status +
                ", type=" + type +
                ", assignedTime=" + assignedTime +
                ", result='" + result + '\'' +
                ", reviewTime=" + reviewTime +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public ReviewJob fork(long id) {
        return new ReviewJob(
                id, reviewContentId, reviewerId,
                operatorId, status, type, assignedTime,
                result, reviewTime
        );
    }

    public final static class Builder {
        private Long jobId = null;
        private String reviewContentId;
        private long reviewerId;
        private long operatorId;
        private ReviewStatus status;
        private ReviewType type;
        private long assignedTime;
        private String result;
        private long reviewTime;

        public Builder() {
        }

        public Builder(ReviewJob job) {
            this.jobId = job.jobId;
            this.reviewContentId = job.reviewContentId;
            this.reviewerId = job.reviewerId;
            this.operatorId = job.operatorId;
            this.status = job.status;
            this.type = job.type;
            this.assignedTime = job.assignedTime;
            this.result = job.result;
            this.reviewTime = job.reviewTime;
        }

        public Builder setJobId(Long jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder setReviewContentId(String reviewContentId) {
            this.reviewContentId = reviewContentId;
            return this;
        }

        public Builder setReviewerId(long reviewerId) {
            this.reviewerId = reviewerId;
            return this;
        }

        public Builder setOperatorId(long operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public Builder setStatus(ReviewStatus status) {
            this.status = status;
            return this;
        }

        public Builder setType(ReviewType type) {
            this.type = type;
            return this;
        }

        public Builder setAssignedTime(long assignedTime) {
            this.assignedTime = assignedTime;
            return this;
        }

        public Builder setResult(String result) {
            this.result = result;
            return this;
        }

        public Builder setReviewTime(long reviewTime) {
            this.reviewTime = reviewTime;
            return this;
        }

        public ReviewJob build() {
            return new ReviewJob(
                    jobId, reviewContentId,
                    reviewerId, operatorId, status, type,
                    assignedTime, result, reviewTime
            );
        }
    }
}
