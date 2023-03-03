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

package space.lingu.lamp.web.domain.comment.repository;

import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.CommentDao;
import space.lingu.lamp.web.domain.comment.Comment;

/**
 * @author RollW
 */
@Repository
public class CommentRepository {
    private final CommentDao commentDao;

    public CommentRepository(LampDatabase lampDatabase) {
        commentDao = lampDatabase.getCommentDao();
    }

    public long insert(Comment comment) {
        return commentDao.insertWithReturn(comment);
    }

    public void update(Comment comment) {
        commentDao.update(comment);
    }

    public Comment getById(long commentId) {
        return commentDao.getById(commentId);
    }
}
