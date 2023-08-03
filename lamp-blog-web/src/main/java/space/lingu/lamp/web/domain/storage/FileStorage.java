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

package space.lingu.lamp.web.domain.storage;

import space.lingu.NonNull;
import space.lingu.lamp.DataItem;
import space.lingu.lamp.EntityBuilder;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(name = "file_storage")
public class FileStorage implements DataItem<FileStorage, String> {
    @DataColumn(name = "file_id")
    @PrimaryKey
    private final String fileId;

    @DataColumn(name = "size")
    private final long fileSize;

    @DataColumn(name = "mime_type")
    private final String mimeType;

    @DataColumn(name = "file_type")
    private final FileType fileType;

    @DataColumn(name = "create_time")
    private final long createTime;

    public FileStorage(String fileId,
                       long fileSize,
                       String mimeType,
                       FileType fileType,
                       long createTime) {
        this.fileId = fileId;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.fileType = fileType;
        this.createTime = createTime;
    }

    public String getFileId() {
        return fileId;
    }

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public String getId() {
        return fileId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public FileType getFileType() {
        return fileType;
    }

    @Override
    public long getUpdateTime() {
        return 0;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileStorage that)) return false;
        return fileSize == that.fileSize && createTime == that.createTime && Objects.equals(fileId, that.fileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileId, fileSize, createTime);
    }

    @Override
    public String toString() {
        return "DiskFileStorage{" +
                "fileId='" + fileId + '\'' +
                ", fileSize=" + fileSize +
                ", createTime=" + createTime +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.STORAGE;
    }

    public static final class Builder implements EntityBuilder<FileStorage, String> {
        private String fileId;
        private long fileSize;
        private String mimeType;
        private FileType fileType;
        private long createTime;

        public Builder() {
        }

        public Builder(FileStorage fileStorage) {
            this.fileId = fileStorage.fileId;
            this.fileSize = fileStorage.fileSize;
            this.mimeType = fileStorage.mimeType;
            this.fileType = fileStorage.fileType;
            this.createTime = fileStorage.createTime;
        }

        @Override
        public Builder setId(String string) {
            return setFileId(string);
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder setMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder setFileType(FileType fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public FileStorage build() {
            return new FileStorage(fileId, fileSize, mimeType, fileType, createTime);
        }
    }
}
