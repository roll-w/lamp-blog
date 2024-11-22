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

package tech.lamprism.lampray.staff.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tech.lamprism.lampray.content.ContentType;
import tech.lamprism.lampray.content.review.ReviewStatus;
import tech.lamprism.lampray.content.review.ReviewerAllocator;
import tech.lamprism.lampray.content.review.persistence.ReviewJobRepository;
import tech.lamprism.lampray.staff.OnStaffEventListener;
import tech.lamprism.lampray.staff.Staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author RollW
 */
@Service
public class ReviewerAllocatorImpl implements ReviewerAllocator, OnStaffEventListener {
   private static final Logger logger = LoggerFactory.getLogger(ReviewerAllocatorImpl.class);

    private final ReviewJobRepository reviewJobRepository;
    private final Map<Long, Integer> weights = new HashMap<>();
    private final TreeMap<Integer, List<Long>> staffReviewingCount = new TreeMap<>();

    public ReviewerAllocatorImpl(ReviewJobRepository reviewJobRepository) {
        this.reviewJobRepository = reviewJobRepository;
        loadStaffReviewingCount();
    }

    private void loadStaffReviewingCount() {
        weights.clear();
        staffReviewingCount.clear();

        // add weight by reviewer
        reviewJobRepository.findByStatus(ReviewStatus.NOT_REVIEWED).forEach(reviewJob -> {
            long reviewerId = reviewJob.getReviewerId();
            int weight = reviewJob.getReviewContentType().getWeight();
            weights.put(reviewerId, weights.getOrDefault(reviewerId, 0) + weight);
        });

        List<Staff> staffs = List.of();
        // TODO: load staffs
        staffs.forEach(staff -> {
            if (weights.containsKey(staff.getUserId())) {
                return;
            }
            weights.put(staff.getUserId(), 0);
        });

        weights.forEach((staffId, weight) -> {
            List<Long> staffIds = staffReviewingCount.getOrDefault(weight,
                    new ArrayList<>());
            staffIds.add(staffId);
            staffReviewingCount.put(weight, staffIds);
        });

        logger.info("Load staff reviewing count: {}", staffReviewingCount);
    }

    private void remappingReviewer(long reviewer, int original, int weight) {
        weights.put(reviewer, weight);

        List<Long> staffIds = staffReviewingCount.get(original);
        staffIds.remove(reviewer);
        if (staffIds.isEmpty()) {
            staffReviewingCount.remove(original);
        }
        staffIds = staffReviewingCount.getOrDefault(weight, new ArrayList<>());
        staffIds.add(reviewer);
        staffReviewingCount.put(weight, staffIds);
    }

    @Override
    public long allocateReviewer(ContentType contentType, boolean allowAutoReviewer) {
        if (canAutoReview(contentType) && allowAutoReviewer) {
            return AUTO_REVIEWER;
        }
        Map.Entry<Integer, List<Long>> entry = staffReviewingCount.firstEntry();
        if (entry == null) {
            return AUTO_REVIEWER;
        }
        List<Long> ids = entry.getValue();
        long reviewerId = ids.get(0);
        remappingReviewer(reviewerId, entry.getKey(), entry.getKey() + contentType.getWeight());
        return reviewerId;
    }

    @Override
    public void releaseReviewer(long reviewerId, ContentType contentType) {
        if (reviewerId == AUTO_REVIEWER) {
            return;
        }
        int weight = weights.get(reviewerId);
        remappingReviewer(reviewerId, weight, weight - contentType.getWeight());
    }

    private boolean canAutoReview(ContentType contentType) {
        return false;
    }

    @Override
    public void onStaffCreated(Staff staff) {
        weights.put(staff.getUserId(), 0);
        List<Long> staffIds =
                staffReviewingCount.getOrDefault(0, new ArrayList<>());
        staffIds.add(staff.getUserId());
        staffReviewingCount.put(0, staffIds);
    }
}
