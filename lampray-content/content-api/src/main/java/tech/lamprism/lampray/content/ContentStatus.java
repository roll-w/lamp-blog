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

package tech.lamprism.lampray.content;

import space.lingu.NonNull;

/**
 * @author RollW
 */
public enum ContentStatus {
    /**
     * Draft, invisible to the public. If the user deletes
     * draft content, it will be recycled for next use.
     */
    DRAFT(0),
    /**
     * Under reviewing.
     */
    REVIEWING(2),
    /**
     * Review rejected.
     */
    REVIEW_REJECTED(3),
    /**
     * Published, visible to public (if public visitable).
     */
    PUBLISHED(1),
    /**
     * Deleted, and invisible to author.
     */
    DELETED(4),
    /**
     * Hide, and invisible to author.
     */
    FORBIDDEN(4),
    /**
     * Hide, but visible to the author.
     */
    HIDE(4),
    ;

    /**
     * The level of status, the higher the level,
     * the more important.
     */
    private final int level;

    ContentStatus(int level) {
        this.level = level;
    }

    public boolean isPublicVisitable() {
        return this == PUBLISHED;
    }

    public boolean needsReview() {
        return this == REVIEWING;
    }

    public boolean canRestore() {
        return this == DELETED || this == FORBIDDEN;
    }

    @NonNull
    public ContentStatus plus(@NonNull ContentStatus status) {
        if (status.level > level) {
            return status;
        }
        return this;
    }
}
