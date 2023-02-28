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

import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentIdentity;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.ContentStatus;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.light.Dao;
import space.lingu.light.DaoConnectionGetter;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.LightRuntimeException;
import space.lingu.light.ManagedConnection;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;
import space.lingu.light.Update;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class ContentMetadataDao implements DaoConnectionGetter {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(ContentMetadata contentMetadata);

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

    @Delete("UPDATE content_metadata SET status = {status} WHERE content_id = {contentId} AND type = {contentType}")
    public abstract void updateStatus(String contentId, ContentType contentType,
                                      ContentStatus status);

    @Query("SELECT * FROM content_metadata WHERE content_id = {contentId} AND type = {contentType}")
    public abstract ContentMetadata getById(String contentId, ContentType contentType);

    @Query("SELECT status FROM content_metadata WHERE content_id IN ({contentIds}) AND type = {contentType}")
    public abstract List<ContentStatus> getStatusByIds(
            List<String> contentIds, ContentType contentType);

    @Query("SELECT id, user_id, content_id, type, status, auth_type FROM content_metadata WHERE content_id IN ({contentIds}) AND type = {contentType}")
    public abstract List<ContentStatus> getMetadataByIds(
            List<String> contentIds, ContentType contentType);

    // @Query("SELECT id, user_id, content_id, type, status, auth_type FROM content_metadata WHERE (content_id, type) IN ({contentIdentities}))")
    // public abstract List<ContentMetadata> getByPair(List<ContentIdentity> contentIdentities);

    public List<ContentMetadata> getByIdentities(List<? extends ContentIdentity> contentIdentities) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT id, user_id, content_id, type, status, auth_type FROM content_metadata WHERE (content_id, type) IN (");
        for (int i = 0; i < contentIdentities.size(); i++) {
            sb.append("(?, ?)");
            if (i != contentIdentities.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        ManagedConnection connection = getConnection();
        PreparedStatement statement = connection.acquire(sb.toString());
        int index = 1;
        for (ContentIdentity identity : contentIdentities) {
            try {
                statement.setString(index++, identity.getContentId());
                statement.setString(index++, identity.getContentType().name());
            } catch (Exception e) {
                throw new LightRuntimeException(e);
            }
        }
        List<ContentMetadata> contentMetadata = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong(1);
                long userId = resultSet.getLong(2);
                String contentId = resultSet.getString(3);
                ContentType type = ContentType.valueOf(resultSet.getString(4));
                ContentStatus status = ContentStatus.valueOf(resultSet.getString(5));
                ContentAccessAuthType authType = ContentAccessAuthType.valueOf(resultSet.getString(6));
                ContentMetadata metadata = new ContentMetadata(
                        id, userId,
                        contentId, type,
                        status, authType);
                contentMetadata.add(metadata);
            }
        } catch (Exception e) {
            throw new LightRuntimeException(e);
        }
        connection.close();
        return contentMetadata;
    }

}
