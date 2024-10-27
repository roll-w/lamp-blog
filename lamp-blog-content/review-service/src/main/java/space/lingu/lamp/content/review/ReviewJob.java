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

package space.lingu.lamp.content.review;

import com.google.common.base.Preconditions;
import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.DataEntity;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.content.ContentAssociated;
import space.lingu.lamp.content.ContentIdentity;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.SimpleContentIdentity;
import tech.rollw.common.web.system.SystemResourceKind;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author RollW
 */
public class ReviewJob implements DataEntity<Long>, ContentAssociated {
    private final Long jobId;

    // TODO: may add serial number to replace job id in the future
    // private String serialNumber;
    private final long reviewContentId;
    private final ContentType reviewContentType;

    // TODO: may change to reviewer ids in the future
    private final Long reviewerId;
    @Nullable
    private final Long operatorId;
    private final ReviewStatus status;
    private final String result;
    private final LocalDateTime assignedTime;
    private final LocalDateTime reviewTime;
    private final ReviewMark reviewMark;
    private final ContentIdentity associatedContent;

    public ReviewJob(Long jobId, long reviewContentId,
                     ContentType reviewContentType, Long reviewerId,
                     @Nullable Long operatorId,
                     ReviewStatus status,
                     String result, LocalDateTime assignedTime,
                     LocalDateTime reviewTime,
                     ReviewMark reviewMark) {
        Preconditions.checkNotNull(status);
        Preconditions.checkNotNull(reviewContentType);
        Preconditions.checkNotNull(reviewMark);

        this.jobId = jobId;
        this.reviewContentId = reviewContentId;
        this.reviewerId = reviewerId;
        this.operatorId = operatorId;
        this.status = status;
        this.reviewContentType = reviewContentType;
        this.assignedTime = assignedTime;
        this.result = result;
        this.reviewTime = reviewTime;
        this.reviewMark = reviewMark;
        this.associatedContent = new SimpleContentIdentity(reviewContentId, reviewContentType);
    }

    @Override
    public Long getId() {
        return getJobId();
    }

    @NonNull
    @Override
    public LocalDateTime getCreateTime() {
        return getAssignedTime();
    }

    @NonNull
    @Override
    public LocalDateTime getUpdateTime() {
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

    public ContentType getReviewContentType() {
        return reviewContentType;
    }

    public LocalDateTime getAssignedTime() {
        return assignedTime;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getReviewTime() {
        return reviewTime;
    }

    public ReviewMark getReviewMark() {
        return reviewMark;
    }

    @Override
    public ContentIdentity getAssociatedContent() {
        return associatedContent;
    }

    public ReviewJob reviewPass(long operatorId, LocalDateTime reviewTime) {
        return new ReviewJob(
                jobId, reviewContentId, reviewContentType,
                reviewerId, operatorId,
                ReviewStatus.REVIEWED, null, assignedTime, reviewTime,
                reviewMark);
    }

    public ReviewJob reviewReject(long operatorId, String result, LocalDateTime reviewTime) {
        return new ReviewJob(
                jobId, reviewContentId, reviewContentType,
                reviewerId, operatorId,
                ReviewStatus.REJECTED, result, assignedTime, reviewTime,
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
        return Objects.equals(reviewerId, job.reviewerId) && Objects.equals(operatorId, job.operatorId) && assignedTime == job.assignedTime && reviewTime == job.reviewTime && Objects.equals(jobId, job.jobId) && Objects.equals(reviewContentId, job.reviewContentId) && status == job.status && reviewContentType == job.reviewContentType && Objects.equals(result, job.result) && reviewMark == job.reviewMark;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobId, reviewContentId, reviewerId, operatorId, status, reviewContentType, assignedTime, result, reviewTime, reviewMark);
    }

    @Override
    public String toString() {
        return "ReviewJob{" +
                "jobId=" + jobId +
                ", reviewContentId='" + reviewContentId + '\'' +
                ", reviewerId=" + reviewerId +
                ", operatorId=" + operatorId +
                ", status=" + status +
                ", type=" + reviewContentType +
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
                id, reviewContentId, reviewContentType,
                reviewerId, operatorId, status, result, assignedTime,
                reviewTime,
                reviewMark);
    }

    @Override
    @NonNull
    public SystemResourceKind getSystemResourceKind() {
        return ReviewJobResourceKind.INSTANCE;
    }


    public final static class Builder implements LongEntityBuilder<ReviewJob> {
        private Long jobId = null;
        private long reviewContentId;
        private Long reviewerId;
        private Long operatorId;
        private ReviewStatus status;
        private ContentType reviewContentType;
        private LocalDateTime assignedTime;
        private String result;
        private LocalDateTime reviewTime;
        private ReviewMark reviewMark;

        public Builder() {
        }

        public Builder(ReviewJob job) {
            this.jobId = job.jobId;
            this.reviewContentId = job.reviewContentId;
            this.reviewerId = job.reviewerId;
            this.operatorId = job.operatorId;
            this.status = job.status;
            this.reviewContentType = job.reviewContentType;
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

        public Builder setReviewContentType(ContentType reviewContentType) {
            this.reviewContentType = reviewContentType;
            return this;
        }

        public Builder setAssignedTime(LocalDateTime assignedTime) {
            this.assignedTime = assignedTime;
            return this;
        }

        public Builder setResult(String result) {
            this.result = result;
            return this;
        }

        public Builder setReviewTime(LocalDateTime reviewTime) {
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
                    reviewContentType, reviewerId, operatorId, status,
                    result, assignedTime, reviewTime,
                    reviewMark);
        }

    }
}
