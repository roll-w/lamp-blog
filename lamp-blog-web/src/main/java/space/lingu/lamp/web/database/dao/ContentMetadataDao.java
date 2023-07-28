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

import space.lingu.lamp.web.domain.content.*;
import space.lingu.light.*;
import tech.rollw.common.web.page.Offset;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface ContentMetadataDao extends AutoPrimaryBaseDao<ContentMetadata>, DaoConnectionGetter {
    @Override
    @Query("SELECT * FROM content_metadata WHERE id = {id}")
    ContentMetadata getById(long id);

    @Override
    @Query("SELECT * FROM content_metadata WHERE id IN ({ids})")
    List<ContentMetadata> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM content_metadata ORDER BY id DESC")
    List<ContentMetadata> get();

    @Override
    @Query("SELECT COUNT(*) FROM content_metadata")
    int count();

    @Override
    @Query("SELECT * FROM content_metadata ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<ContentMetadata> get(Offset offset);

    @Override
    default String getTableName() {
        return "content_metadata";
    }

    @Delete("UPDATE content_metadata SET status = {status} WHERE content_id = {contentId} AND type = {contentType}")
    void updateStatus(long contentId, ContentType contentType,
                      ContentStatus status);

    @Query("SELECT * FROM content_metadata WHERE content_id = {contentId} AND type = {contentType}")
    ContentMetadata getById(long contentId, ContentType contentType);

    @Query("SELECT status FROM content_metadata WHERE content_id IN ({contentIds}) AND type = {contentType}")
    List<ContentStatus> getStatusByIds(
            List<Long> contentIds, ContentType contentType);

    @Query("SELECT id, user_id, content_id, type, status, auth_type FROM content_metadata WHERE content_id IN ({contentIds}) AND type = {contentType}")
    List<ContentStatus> getMetadataByIds(
            List<Long> contentIds, ContentType contentType);

    default List<ContentMetadata> getByIdentities(List<? extends ContentIdentity> contentIdentities) {
        if (contentIdentities.isEmpty()) {
            return List.of();
        }

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
                statement.setLong(index++, identity.getContentId());
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
                long contentId = resultSet.getLong(3);
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
