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

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.ArticleDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.article.Article;
import tech.rollw.common.web.page.Offset;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class ArticleRepository extends AutoPrimaryBaseRepository<Article> {
    private final ArticleDao articleDao;

    public ArticleRepository(LampDatabase lampDatabase,
                             ContextThreadAware<PageableContext> pageableContextThreadAware,
                             CacheManager cacheManager) {
        super(lampDatabase.getArticleDao(), pageableContextThreadAware, cacheManager);
        this.articleDao = lampDatabase.getArticleDao();
    }

    public boolean isTitleExist(long userId, String title) {
        String s = articleDao.getArticleTitle(title, userId);
        return s != null;
    }

    public Long getUserIdByArticleId(long articleId) {
        return articleDao.getUserIdByArticleId(articleId);
    }

    public boolean isArticleExist(long articleId) {
        return articleDao.getUserIdByArticleId(articleId) != null;
    }

    // TODO: process pages at repository layer
    public List<Article> getArticlesByUser(long userId,
                                           Offset offset) {
        return articleDao.getByUserId(userId, offset);
    }

    public List<Article> getArticlesByUser(long userId) {
        return articleDao.getByUserId(userId);
    }

    @Override
    protected Class<Article> getEntityClass() {
        return Article.class;
    }
}
