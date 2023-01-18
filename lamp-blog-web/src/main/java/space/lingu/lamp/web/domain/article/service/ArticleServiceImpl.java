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

package space.lingu.lamp.web.domain.article.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import space.lingu.lamp.Result;
import space.lingu.lamp.web.common.DataErrorCode;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.article.ArticleStatus;
import space.lingu.lamp.web.domain.article.dto.ArticleInfo;
import space.lingu.lamp.web.domain.article.event.OnArticlePublishEvent;
import space.lingu.lamp.web.domain.article.repository.ArticleRepository;

import javax.validation.constraints.NotEmpty;

/**
 * @author RollW
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ArticleServiceImpl(ArticleRepository articleRepository,
                              ApplicationEventPublisher applicationEventPublisher) {
        this.articleRepository = articleRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public Result<ArticleInfo> publishArticle(long userId,
                                              @NotEmpty String title,
                                              String content) {
        if (articleRepository.isTitleExist(userId, title)) {
            return Result.of(DataErrorCode.ERROR_DATA_EXISTED);
        }
        long createTime = System.currentTimeMillis();
        Article.Builder articleBuilder = Article.builder()
                .setUserId(userId)
                .setTitle(title)
                .setContent(content)
                .setCreateTime(createTime)
                .setUpdateTime(createTime)
                .setStatus(ArticleStatus.REVIEWING);
        Article article = articleBuilder.build();
        article = articleRepository.createArticle(article);
        OnArticlePublishEvent articlePublishEvent =
                new OnArticlePublishEvent(article);
        applicationEventPublisher.publishEvent(articlePublishEvent);

        return Result.success(
                ArticleInfo.from(article)
        );
    }

    @Override
    public Result<Void> deleteArticle(long articleId) {
        return Result.success();
    }

    @Override
    public Result<Void> updateArticle(long articleId) {
        return Result.success();
    }

    @Override
    public Article getArticle(long articleId) {
        return null;
    }
}
