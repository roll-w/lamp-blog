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

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.CommentDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.comment.Comment;
import space.lingu.lamp.content.ContentType;
import tech.rollw.common.web.page.Offset;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class CommentRepository extends AutoPrimaryBaseRepository<Comment> {
    private final CommentDao commentDao;

    public CommentRepository(LampDatabase lampDatabase,
                             ContextThreadAware<PageableContext> pageableContextThreadAware,
                             CacheManager cacheManager) {
        super(lampDatabase.getCommentDao(), pageableContextThreadAware, cacheManager);
        this.commentDao = lampDatabase.getCommentDao();
    }

    @Override
    protected Class<Comment> getEntityClass() {
        return Comment.class;
    }

    public List<Comment> getComments(long collectionId,
                                     ContentType contentType,
                                     Offset offset) {
        return commentDao.getCommentsOn(
                collectionId,
                contentType,
                offset
        );
    }

    public List<Comment> getComments(long collectionId,
                                     ContentType contentType) {
        return commentDao.getCommentsOn(collectionId, contentType);
    }
}