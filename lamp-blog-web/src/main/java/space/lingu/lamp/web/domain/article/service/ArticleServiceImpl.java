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
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.Result;
import space.lingu.lamp.web.common.ParamValidate;
import space.lingu.lamp.web.common.WebCommonErrorCode;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.article.common.ArticleErrorCode;
import space.lingu.lamp.web.domain.article.repository.ArticleRepository;
import space.lingu.lamp.web.domain.content.BasicContentInfo;
import space.lingu.lamp.web.domain.content.ContentAccessor;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentPublisher;
import space.lingu.lamp.web.domain.content.UncreatedContent;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;

/**
 * @author RollW
 */
@Service
public class ArticleServiceImpl implements ArticleService,
        ContentAccessor<Article>, ContentPublisher {
    private static final Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private final ArticleRepository articleRepository;

    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Result<Void> deleteArticle(long articleId) {
        if (!articleRepository.isArticleExist(articleId)) {
            return Result.of(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        // TODO: replace with ArticleInfo
        Article article = articleRepository.get(articleId);
        BasicContentInfo contentInfo = new BasicContentInfo(
                article.getUserId(),
                String.valueOf(articleId),
                ContentType.ARTICLE
        );

        return Result.success();
    }

    @Override
    public Result<Void> updateArticle(long articleId) {
        return Result.success();
    }

    @Override
    public Article getArticle(long articleId) {
        return articleRepository.get(articleId);
    }

    @Override
    public Article getContent(String contentId, ContentType contentType) {
        if (contentType != ContentType.ARTICLE) {
            return null;
        }
        try {
            return articleRepository.get(Long.parseLong(contentId));
        } catch (NumberFormatException e) {
            throw new ContentException(WebCommonErrorCode.ERROR_PARAM_FAILED, e);
        }
    }

    @Override
    public ContentDetails publish(@NonNull UncreatedContent uncreatedContent, long timestamp) {
        if (uncreatedContent.getContentType() != ContentType.ARTICLE) {
            throw new IllegalArgumentException("Content type not supported: " +
                    uncreatedContent.getContentType());
        }
        if (articleRepository.isTitleExist(
                uncreatedContent.getUserId(),
                uncreatedContent.getTitle())) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_EXISTED);
        }
        ParamValidate.notEmpty(uncreatedContent.getTitle(),
                () -> new ContentException(ArticleErrorCode.ERROR_TITLE_EMPTY));
        ParamValidate.notEmpty(uncreatedContent.getContent(),
                () -> new ContentException(ArticleErrorCode.ERROR_CONTENT_EMPTY));

        Article article = Article.builder()
                .setUserId(uncreatedContent.getUserId())
                .setTitle(uncreatedContent.getTitle())
                .setContent(uncreatedContent.getContent())
                .setCreateTime(timestamp)
                .setUpdateTime(timestamp)
                .build();
        Article created = articleRepository.createArticle(article);
        logger.trace("article created: {}.", created);
        return created;
    }

    @Override
    public boolean supports(ContentType contentType) {
        return contentType == ContentType.ARTICLE;
    }
}
