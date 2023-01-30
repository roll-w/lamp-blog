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
import space.lingu.lamp.web.domain.article.dto.ArticleInfo;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class ArticleDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(Article... articles);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract long insert(Article article);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(List<Article> articles);

    @Delete
    protected abstract void delete(Article Article);

    @Delete
    protected abstract void delete(List<Article> articles);

    @Delete("DELETE FROM article")
    protected abstract void clearTable();

    @Query("SELECT * FROM article")
    public abstract List<Article> get();

    @Query("SELECT * FROM article LIMIT {limit} OFFSET {offset}")
    public abstract List<Article> get(int limit, int offset);

    @Query("SELECT title FROM article WHERE user_id = {userId} AND title = {title}")
    public abstract String getArticleTitle(String title, long userId);

    @Query("SELECT title FROM article WHERE id = {articleId}")
    public abstract String getArticleContent(long articleId);

    @Query("SELECT * FROM article WHERE id = {id}")
    public abstract Article getById(long id);

    @Query("SELECT * FROM article WHERE user_id = {userId}")
    public abstract List<Article> getByUserId(long userId);

    @Query("SELECT * FROM article WHERE user_id = {userId} LIMIT {limit} OFFSET {offset}")
    public abstract List<Article> getByUserId(long userId, int limit, int offset);

    @Query("SELECT `id`, `user_id`, `title`, `cover`, `create_time`, `update_time` " +
            "FROM article LIMIT {limit} OFFSET {offset}")
    public abstract List<ArticleInfo> getArticleInfos(int limit, int offset);

    @Query("SELECT `id`, `user_id`, `title`, `cover`, `create_time`, `update_time` " +
            "FROM article WHERE user_id = {userId} LIMIT {limit} OFFSET {offset}")
    public abstract List<ArticleInfo> getArticleInfosByUser(long userId, int limit, int offset);
}
