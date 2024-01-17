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
import space.lingu.lamp.web.common.ParamValidate;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.article.common.ArticleErrorCode;
import space.lingu.lamp.web.domain.article.repository.ArticleRepository;
import space.lingu.lamp.web.domain.content.*;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionAccessor;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import tech.rollw.common.web.page.ImmutablePage;
import tech.rollw.common.web.page.Offset;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class ArticleService implements
        ContentAccessor, ContentPublisher, ContentCollectionAccessor {
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public Article getContent(long contentId, ContentType contentType) {
        if (contentType != ContentType.ARTICLE) {
            return null;
        }
        return articleRepository.getById(contentId);
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
        Article created = articleRepository.insert(article);
        logger.trace("article created: {}.", created);
        return created;
    }

    @Override
    public boolean supports(@NonNull ContentType contentType) {
        return contentType == ContentType.ARTICLE;
    }

    @Override
    public Page<Article> getContentCollection(
            ContentCollectionType contentCollectionType,
            long collectionId,
            Pageable pageable) {
        if (!supportsCollection(contentCollectionType)) {
            throw new IllegalArgumentException("Content collection type not supported: " +
                    contentCollectionType);
        }
        Offset offset = pageable.toOffset();
        switch (contentCollectionType) {
            case ARTICLES -> {
                return getArticles(offset);
            }
            case USER_ARTICLES -> {
                return getUserArticles(collectionId, offset);
            }
        }
        // TODO: implement
        return ImmutablePage.of();
    }


    @Override
    public List<Article> getContentCollection(
            ContentCollectionType contentCollectionType,
            long collectionId) {
        if (!supportsCollection(contentCollectionType)) {
            throw new IllegalArgumentException("Content collection type not supported: " +
                    contentCollectionType);
        }
        switch (contentCollectionType) {
            case ARTICLES -> {
                return getArticles();
            }
            case USER_ARTICLES -> {
                return getUserArticles(collectionId);
            }
        }
        return List.of();
    }

    private Page<Article> getUserArticles(long id,
                                          Offset offset) {
        return ImmutablePage.of(
                offset,
                1,
                articleRepository.getArticlesByUser(
                        id,
                        offset
                )
        );
    }

    private List<Article> getUserArticles(long id) {
        return articleRepository.getArticlesByUser(id);
    }

    private Page<Article> getArticles(Offset offset) {
        return ImmutablePage.of(
                offset, 1,
                articleRepository.get(offset)
        );
    }

    private List<Article> getArticles() {
        return articleRepository.get();
    }
}
