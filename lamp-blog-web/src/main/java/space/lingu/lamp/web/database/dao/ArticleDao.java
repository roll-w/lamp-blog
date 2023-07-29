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

import space.lingu.lamp.web.domain.article.Article;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface ArticleDao extends AutoPrimaryBaseDao<Article> {
    @Override
    @Query("SELECT * FROM article WHERE id = {id}")
    Article getById(long id);

    @Override
    @Query("SELECT * FROM article WHERE id IN ({ids})")
    List<Article> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM article ORDER BY id DESC")
    List<Article> get();

    @Override
    @Query("SELECT COUNT(*) FROM article")
    int count();

    @Override
    @Query("SELECT * FROM article ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<Article> get(Offset offset);

    @Override
    @Delete("DELETE FROM article WHERE id = {id}")
    void deleteById(Long id);

    @Override
    @Delete("DELETE FROM article WHERE id IN ({ids})")
    void deleteByIds(List<Long> ids);

    @Override
    default String getTableName() {
        return "article";
    }

    @Query("SELECT * FROM article WHERE title = {title} AND user_id = {userId}")
    Article getByTitle(String title, long userId);

    @Query("SELECT title FROM article WHERE user_id = {userId} AND title = {title}")
    String getArticleTitle(String title, long userId);

    @Query("SELECT user_id FROM article WHERE id = {articleId}")
    Long getUserIdByArticleId(long articleId);

    @Query("SELECT title FROM article WHERE id = {articleId}")
    String getArticleContent(long articleId);

    @Query("SELECT * FROM article WHERE user_id = {userId}")
    List<Article> getByUserId(long userId);

    @Query("SELECT * FROM article WHERE user_id = {userId} " +
            "LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<Article> getByUserId(long userId, Offset offset);
}

