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
import space.lingu.lamp.data.page.Offset;
import space.lingu.lamp.data.page.PageHelper;
import space.lingu.lamp.web.common.ParamValidate;
import space.lingu.lamp.web.common.WebCommonErrorCode;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.article.common.ArticleErrorCode;
import space.lingu.lamp.web.domain.article.repository.ArticleRepository;
import space.lingu.lamp.web.domain.content.ContentAccessor;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentPublisher;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.UncreatedContent;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionProvider;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ArticleService implements
        ContentAccessor, ContentPublisher, ContentCollectionProvider {
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
    public ContentDetails publish(@NonNull UncreatedContent uncreatedContent, long timestamp)
            throws ContentException {
        if (uncreatedContent.getContentType() != ContentType.ARTICLE) {
            throw new IllegalArgumentException("Content type not supported: " +
                    uncreatedContent.getContentType());
        }
        String title = ParamValidate.notEmpty(uncreatedContent.getTitle(),
                () -> new ContentException(ArticleErrorCode.ERROR_TITLE_EMPTY));
        String content = ParamValidate.notEmpty(uncreatedContent.getContent(),
                () -> new ContentException(ArticleErrorCode.ERROR_CONTENT_EMPTY));
        if (articleRepository.isTitleExist(
                uncreatedContent.getUserId(), title)) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_EXISTED);
        }

        Article article = Article.builder()
                .setUserId(uncreatedContent.getUserId())
                .setTitle(title)
                .setContent(content)
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

    @Override
    public List<Article> getContentCollection(
            ContentCollectionType contentCollectionType,
            String collectionId,
            int page, int size) {
        if (!supportsCollection(contentCollectionType)) {
            throw new IllegalArgumentException("Content collection type not supported: " +
                    contentCollectionType);
        }

        Offset offset = PageHelper.offset(page, size);
        switch (contentCollectionType) {
            case ARTICLES -> {
                return getArticles(offset);
            }
            case USER_ARTICLES -> {
                return getUserArticles(collectionId, offset);
            }
        }
        // TODO: implement
        return List.of();
    }

    private List<Article> getUserArticles(String id,
                                          Offset offset) {
        long userId;
        try {
            userId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ContentException(WebCommonErrorCode.ERROR_PARAM_FAILED, e);
        }

        return articleRepository.getArticlesByUser(
                userId, offset.offset(), offset.limit());
    }

    private List<Article> getArticles(Offset offset) {
        return articleRepository.getArticles(
                offset.offset(),
                offset.limit()
        );
    }
}
