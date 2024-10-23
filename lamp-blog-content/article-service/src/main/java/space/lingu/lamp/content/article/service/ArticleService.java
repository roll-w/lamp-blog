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

package space.lingu.lamp.content.article.service;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentPublisher;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.UncreatedContent;
import space.lingu.lamp.content.article.Article;
import space.lingu.lamp.content.article.persistence.ArticleDo;
import space.lingu.lamp.content.article.persistence.ArticleRepository;
import space.lingu.lamp.content.collection.ContentCollectionIdentity;
import space.lingu.lamp.content.collection.ContentCollectionProvider;
import space.lingu.lamp.content.collection.ContentCollectionType;
import space.lingu.lamp.content.common.ContentErrorCode;
import space.lingu.lamp.content.common.ContentException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author RollW
 */
@Service
public class ArticleService implements ContentPublisher, ContentCollectionProvider {
    private static final Logger logger = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public ContentDetails publish(@NonNull UncreatedContent uncreatedContent,
                                  LocalDateTime timestamp)
            throws ContentException {
        if (uncreatedContent.getContentType() != ContentType.ARTICLE) {
            throw new IllegalArgumentException("Content type not supported: " +
                    uncreatedContent.getContentType());
        }
        String title = Validate.notEmpty(uncreatedContent.getTitle());
        String content = Validate.notEmpty(uncreatedContent.getContent());

        if (articleRepository.findByTitle(
                title, uncreatedContent.getUserId()).isPresent()) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_EXISTED);
        }

        ArticleDo article = ArticleDo.builder()
                .setUserId(uncreatedContent.getUserId())
                .setTitle(title)
                .setContent(content)
                .setCreateTime(timestamp)
                .setUpdateTime(timestamp)
                .build();
        ArticleDo created = articleRepository.save(article);
        logger.trace("Article({}) title={} created by user({})",
                created.getId(), created.getTitle(), created.getUserId());
        return created;
    }

    @Override
    public boolean supports(@NonNull ContentType contentType) {
        return contentType == ContentType.ARTICLE;
    }

    private List<Article> getUserArticles(long id) {
        return articleRepository.findAllByUserId(id)
                .stream()
                .map(ArticleDo::lock)
                .toList();
    }

    private List<Article> getArticles() {
        return articleRepository.findAll()
                .stream()
                .map(ArticleDo::lock)
                .toList();
    }

    @Override
    public boolean supportsCollection(ContentCollectionType contentCollectionType) {
        return switch (contentCollectionType) {
            case ARTICLES, USER_ARTICLES -> true;
            default -> false;
        };
    }

    @NonNull
    @Override
    public List<? extends ContentDetails> getContents(
            ContentCollectionIdentity contentCollectionIdentity) {
        return switch (contentCollectionIdentity.getContentCollectionType()) {
            case ARTICLES -> getArticles();
            case USER_ARTICLES -> getUserArticles(
                    contentCollectionIdentity.getContentCollectionId()
            );
            default -> throw new UnsupportedOperationException("Unsupported collection type: " +
                    contentCollectionIdentity.getContentCollectionType());
        };
    }
}
