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

package space.lingu.lamp.web.domain.article.repository;

import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.ArticleDao;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.article.ArticleStatus;

/**
 * @author RollW
 */
@Repository
public class ArticleRepository {
    private final ArticleDao articleDao;

    public ArticleRepository(LampDatabase lampDatabase) {
        this.articleDao = lampDatabase.getArticleDao();
    }

    public boolean isTitleExist(long userId, String title) {
        String s = articleDao.getArticleTitle(title, userId);
        return s != null;
    }

    public Article createArticle(Article article) {
        long id = articleDao.insert(article);
        return article.fork(id);
    }

    public void deleteArticle(long id) {
        articleDao.setArticleStatus(id, ArticleStatus.DELETED);
        // mark article as deleted
    }
}
