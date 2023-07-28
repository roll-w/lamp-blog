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

import space.lingu.lamp.web.domain.content.collection.ContentCollectionMetadata;
import space.lingu.light.Dao;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface ContentCollectionMetadataDao extends AutoPrimaryBaseDao<ContentCollectionMetadata> {
    @Override
    @Query("SELECT * FROM content_collection_metadata WHERE id = {id}")
    ContentCollectionMetadata getById(long id);

    @Override
    @Query("SELECT * FROM content_collection_metadata WHERE id IN ({ids})")
    List<ContentCollectionMetadata> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM content_collection_metadata ORDER BY id DESC")
    List<ContentCollectionMetadata> get();

    @Override
    @Query("SELECT COUNT(*) FROM content_collection_metadata")
    int count();

    @Override
    @Query("SELECT * FROM content_collection_metadata ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<ContentCollectionMetadata> get(Offset offset);

    @Override
    default String getTableName() {
        return "content_collection_metadata";
    }

    @Query("SELECT * FROM content_collection_metadata WHERE user_id = {userId}")
    List<ContentCollectionMetadata> getByUserId(long userId);
}
