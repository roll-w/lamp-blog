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

package space.lingu.lamp.content.service;

import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentDetailsMetadata;
import space.lingu.lamp.content.ContentMetadata;
import space.lingu.lamp.content.ContentOperator;
import space.lingu.lamp.content.ContentStatus;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.common.ContentErrorCode;
import space.lingu.lamp.content.common.ContentException;
import tech.rollw.common.web.CommonRuntimeException;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * @author RollW
 */
public abstract class AbstractContentOperator implements ContentOperator {
    private ContentMetadata contentMetadata;

    private ContentDetails content;
    private boolean checkDeleted;
    private boolean autoUpdateEnabled = true;
    private boolean updateFlag = false;

    private final ContentMetadataService contentMetadataService;

    protected AbstractContentOperator(
            ContentDetails contentDetails,
            ContentMetadataService contentMetadataService,
            boolean checkDeleted) {
        this.contentMetadataService = contentMetadataService;
        this.content = contentDetails;
        this.checkDeleted = checkDeleted;
    }

    @NonNull
    @Override
    public final OffsetDateTime getCreateTime() {
        return content.getCreateTime();
    }

    @NonNull
    @Override
    public final OffsetDateTime getUpdateTime() {
        return content.getUpdateTime();
    }

    @Override
    public final long getContentId() {
        return content.getContentId();
    }

    @Override
    @NonNull
    public final ContentType getContentType() {
        return content.getContentType();
    }

    @Override
    public final long getUserId() {
        return content.getUserId();
    }

    @Override
    @Nullable
    public final String getTitle() {
        return content.getTitle();
    }

    @Override
    @Nullable
    public final String getContent() {
        return content.getContent();
    }

    @Override
    @Nullable
    public final ContentDetailsMetadata getMetadata() {
        return content.getMetadata();
    }

    @Override
    public final ContentOperator rename(String newName)
            throws CommonRuntimeException {
        if (Objects.equals(newName, content.getTitle())) {
            return this;
        }
        if (setNameInternal(newName)) {
            setUpdated();
            return updateInternal();
        }
        return this;
    }

    @Override
    public final ContentOperator setContent(String content)
            throws CommonRuntimeException {
        if (Objects.equals(content, this.content.getContent())) {
            return this;
        }
        if (setContentInternal(content)) {
            setUpdated();
            return updateInternal();
        }
        return this;
    }

    @Override
    public final ContentOperator setPassword(String password)
            throws CommonRuntimeException {
        return this;
    }

    @Override
    public final ContentOperator setMetadata(ContentDetailsMetadata metadata) throws CommonRuntimeException {
        if (Objects.equals(metadata, this.content.getMetadata())) {
            return this;
        }
        if (setMetadataInternal(metadata)) {
            setUpdated();
            return updateInternal();
        }
        return this;
    }

    protected abstract boolean setNameInternal(@Nullable String name) throws CommonRuntimeException;

    protected abstract boolean setContentInternal(@Nullable String content) throws CommonRuntimeException;

    protected abstract boolean setMetadataInternal(@Nullable ContentDetailsMetadata metadata) throws CommonRuntimeException;

    @Override
    public final ContentOperator update() throws CommonRuntimeException {
        if (!updateFlag) {
            return this;
        }
        updateContent();
        return this;
    }

    @Override
    public final ContentOperator delete() throws CommonRuntimeException {
        checkIfDeleted();

        ContentMetadata metadata = loadMetadata();
        ContentMetadata updated = metadata.toBuilder()
                .setContentStatus(ContentStatus.DELETED)
                .build();

        contentMetadataService.updateMetadata(updated);
        reloadContentMetadata(updated);

        // ContentMetadata do not record update time,
        // so we need to update comment to record update time.
        return updateInternal();
    }

    @Override
    public final ContentOperator forbidden() throws CommonRuntimeException {
        checkIfDeleted();

        ContentMetadata metadata = loadMetadata();
        ContentMetadata updated = metadata.toBuilder()
                .setContentStatus(ContentStatus.FORBIDDEN)
                .build();
        contentMetadataService.updateMetadata(updated);
        reloadContentMetadata(updated);

        return updateInternal();
    }

    @Override
    public final ContentOperator restore() throws CommonRuntimeException {
        checkIfDeleted();

        ContentMetadata metadata = loadMetadata();
        ContentMetadata updated = metadata.toBuilder()
                .setContentStatus(ContentStatus.REVIEWING)
                .build();
        contentMetadataService.updateMetadata(updated);
        reloadContentMetadata(updated);

        return updateInternal();
    }

    @Override
    public final ContentOperator disableAutoUpdate() {
        this.autoUpdateEnabled = false;
        return this;
    }

    @Override
    public final ContentOperator enableAutoUpdate() {
        this.autoUpdateEnabled = true;
        return this;
    }

    @Override
    public final boolean isAutoUpdateEnabled() {
        return autoUpdateEnabled;
    }

    @Override
    public final void setCheckDeleted(boolean checkDeleted) {
        this.checkDeleted = checkDeleted;
    }

    @Override
    public final boolean isCheckDeleted() {
        return checkDeleted;
    }

    protected final AbstractContentOperator updateInternal() {
        if (!autoUpdateEnabled) {
            updateFlag = true;
            return this;
        }
        content = updateContent();
        updateFlag = false;
        return this;
    }

    protected final void setUpdated() {
        updateFlag = true;
    }

    /**
     * Set update time of content then update
     * content to the database.
     */
    protected abstract ContentDetails updateContent();

    protected final void checkIfDeleted() {
        if (!checkDeleted) {
            return;
        }
        ContentMetadata metadata = loadMetadata();
        if (metadata.getContentStatus() == ContentStatus.DELETED) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_DELETED);
        }
    }

    private ContentMetadata loadMetadata() {
        if (contentMetadata == null) {
            contentMetadata = contentMetadataService.getMetadata(content);
        }
        return contentMetadata;
    }

    private void reloadContentMetadata(ContentMetadata metadata) {
        this.contentMetadata = metadata;
    }
 }
