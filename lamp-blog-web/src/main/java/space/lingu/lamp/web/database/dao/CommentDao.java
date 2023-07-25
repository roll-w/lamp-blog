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
import space.lingu.light.Delete;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class CommentDao implements LampDao<Comment> {
    @Delete("DELETE FROM comment")
    protected abstract void clearTable();

    @Query("SELECT * FROM comment")
    public abstract List<Comment> get();

    @Query("SELECT id, user_id, comment_on_id, parent_id, content, create_time, type, status " +
            "FROM comment ORDER BY id " +
            "LIMIT {limit} OFFSET {offset}")
    public abstract List<Comment> get(int offset, int limit);

    @Query("SELECT * FROM comment WHERE id = {id}")
    public abstract Comment getById(long id);

    @Query("SELECT * FROM comment WHERE comment_on_id = {contentId} AND type = {contentType}")
    public abstract List<Comment> getCommentsOn(String contentId, ContentType contentType);

    @Query("SELECT * FROM comment WHERE comment_on_id = {contentId} AND type = {contentType} " +
            "LIMIT {offset.limit()} OFFSET {offset.offset()}")
    public abstract List<Comment> getCommentsOn(String contentId, ContentType contentType, Offset offset);
}


