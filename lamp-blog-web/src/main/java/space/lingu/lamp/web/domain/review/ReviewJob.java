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

import com.google.common.base.Preconditions;
import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.content.ContentAssociated;
import space.lingu.lamp.content.ContentIdentity;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.SimpleContentIdentity;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.Constructor;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.LightIgnore;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(name = "review_job")
public class ReviewJob implements LongDataItem<ReviewJob>, ContentAssociated {
    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long jobId;

    // TODO: may add serial number to replace job id in the future
    // private String serialNumber;

    @DataColumn(name = "content_id")
    private final long reviewContentId;

    // TODO: may change to reviewer ids in the future
    @DataColumn(name = "reviewer_id")
    private final Long reviewerId;

    @DataColumn(name = "operator_id")
    @Nullable
    private final Long operatorId;

    @DataColumn(name = "status")
    private final ReviewStatus status;

    @DataColumn(name = "type")
    private final ContentType type;

    @DataColumn(name = "create_time")
    private final long assignedTime;

    @DataColumn(name = "result")
    private final String result;

    @DataColumn(name = "review_time")
    private final long reviewTime;

    @DataColumn(name = "review_mark")
    private final ReviewMark reviewMark;

    @LightIgnore
    private final ContentIdentity associatedContent;

    @Constructor
    public ReviewJob(Long jobId, long reviewContentId,
                     Long reviewerId,
                     @Nullable Long operatorId,
                     ReviewStatus status, ContentType type,
                     long assignedTime, String result, long reviewTime,
                     ReviewMark reviewMark) {
        Preconditions.checkNotNull(status);
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(reviewMark);

        this.jobId = jobId;
        this.reviewContentId = reviewContentId;
        this.reviewerId = reviewerId;
        this.operatorId = operatorId;
        this.status = status;
        this.type = type;
        this.assignedTime = assignedTime;
        this.result = result;
        this.reviewTime = reviewTime;
        this.reviewMark = reviewMark;
        this.associatedContent = new SimpleContentIdentity(reviewContentId, type);
    }

    public ReviewJob(long reviewContentId, long reviewerId,
                     long operatorId, ReviewStatus status, ContentType type,
                     long assignedTime, String result, long reviewTime,
                     ReviewMark reviewMark) {
        this(null, reviewContentId, reviewerId, operatorId, status, type,
                assignedTime, result, reviewTime, reviewMark);
    }

    @Override
    public Long getId() {
        return getJobId();
    }

    @Override
    public long getCreateTime() {
        return getAssignedTime();
    }

    @Override
    public long getUpdateTime() {
        return getReviewTime();
    }

    public Long getJobId() {
        return jobId;
    }

    public long getReviewContentId() {
        return reviewContentId;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    @Nullable
    public Long getOperatorId() {
        return operatorId;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public ContentType getType() {
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

    public ReviewMark getReviewMark() {
        return reviewMark;
    }

    @Override
    public ContentIdentity getAssociatedContent() {
        return associatedContent;
    }

    public ReviewJob reviewPass(long operatorId, long reviewTime) {
        return new ReviewJob(
                jobId, reviewContentId, reviewerId,
                operatorId, ReviewStatus.REVIEWED,
                type, assignedTime, null, reviewTime,
                reviewMark);
    }

    public ReviewJob reviewReject(long operatorId, String result, long reviewTime) {
        return new ReviewJob(
                jobId, reviewContentId, reviewerId,
                operatorId, ReviewStatus.REJECTED,
                type, assignedTime, result, reviewTime,
                reviewMark);
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewJob job = (ReviewJob) o;
        return Objects.equals(reviewerId, job.reviewerId) && Objects.equals(operatorId, job.operatorId) && assignedTime == job.assignedTime && reviewTime == job.reviewTime && Objects.equals(jobId, job.jobId) && Objects.equals(reviewContentId, job.reviewContentId) && status == job.status && type == job.type && Objects.equals(result, job.result) && reviewMark == job.reviewMark;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, reviewContentId, reviewerId, operatorId, status, type, assignedTime, result, reviewTime, reviewMark);
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
                ", reviewMark=" + reviewMark +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public ReviewJob fork(long id) {
        return new ReviewJob(
                id, reviewContentId, reviewerId,
                operatorId, status, type, assignedTime,
                result, reviewTime,
                reviewMark);
    }

    @Override
    @NonNull
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.REVIEW_JOB;
    }


    public final static class Builder implements LongEntityBuilder<ReviewJob> {
        private Long jobId = null;
        private long reviewContentId;
        private Long reviewerId;
        private Long operatorId;
        private ReviewStatus status;
        private ContentType type;
        private long assignedTime;
        private String result;
        private long reviewTime;
        private ReviewMark reviewMark;

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
            this.reviewMark = job.reviewMark;
        }

        @Override
        public LongEntityBuilder<ReviewJob> setId(Long id) {
            return setJobId(id);
        }

        public Builder setJobId(Long jobId) {
            this.jobId = jobId;
            return this;
        }

        public Builder setReviewContentId(long reviewContentId) {
            this.reviewContentId = reviewContentId;
            return this;
        }

        public Builder setReviewerId(Long reviewerId) {
            this.reviewerId = reviewerId;
            return this;
        }

        public Builder setOperatorId(Long operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public Builder setStatus(ReviewStatus status) {
            this.status = status;
            return this;
        }

        public Builder setType(ContentType type) {
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

        public Builder setReviewMark(ReviewMark reviewMark) {
            this.reviewMark = reviewMark;
            return this;
        }

        @Override
        public ReviewJob build() {
            return new ReviewJob(
                    jobId, reviewContentId,
                    reviewerId, operatorId, status, type,
                    assignedTime, result, reviewTime,
                    reviewMark);
        }

    }
}
