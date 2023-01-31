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

import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
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
public abstract class ContentMetadataDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(ContentMetadata... contentMetadata);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(List<ContentMetadata> contentMetadata);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(ContentMetadata... contentMetadata);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(List<ContentMetadata> contentMetadata);

    @Delete
    protected abstract void delete(ContentMetadata ContentMetadata);

    @Delete
    protected abstract void delete(List<ContentMetadata> contentMetadata);

    @Delete("DELETE FROM content_metadata")
    protected abstract void clearTable();

    @Query("SELECT * FROM content_metadata")
    public abstract List<ContentMetadata> get();

    @Delete("UPDATE content_metadata SET status = {status} WHERE content_id = {contentId} AND content_type = {contentType}")
    public abstract void updateStatus(String contentId, ContentType contentType,
                                      ContentStatus status);

    @Query("SELECT * FROM content_metadata WHERE content_id = {contentId} AND content_type = {contentType}")
    public abstract ContentMetadata getById(String contentId, ContentType contentType);

}
