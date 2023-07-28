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

import space.lingu.lamp.web.domain.comment.Comment;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.light.Dao;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface CommentDao extends AutoPrimaryBaseDao<Comment> {
    @Override
    @Query("SELECT * FROM comment WHERE id = {id}")
    Comment getById(long id);

    @Override
    @Query("SELECT * FROM comment WHERE id IN ({ids})")
    List<Comment> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM comment ORDER BY id DESC")
    List<Comment> get();

    @Override
    @Query("SELECT COUNT(*) FROM comment")
    int count();

    @Override
    @Query("SELECT * FROM comment ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<Comment> get(Offset offset);

    @Override
    default String getTableName() {
        return "comment";
    }

    @Query("SELECT * FROM comment WHERE comment_on_id = {contentId} AND type = {contentType}")
    List<Comment> getCommentsOn(long contentId, ContentType contentType);

    @Query("SELECT * FROM comment WHERE comment_on_id = {contentId} AND type = {contentType} " +
            "LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<Comment> getCommentsOn(long contentId, ContentType contentType, Offset offset);
}
