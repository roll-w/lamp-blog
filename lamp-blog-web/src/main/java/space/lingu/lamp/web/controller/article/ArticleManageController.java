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

package space.lingu.lamp.web.controller.article;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.lingu.lamp.web.controller.AdminApi;
import space.lingu.lamp.web.domain.article.vo.ArticleMetaVo;
import space.lingu.lamp.web.domain.content.ContentAccessService;
import space.lingu.lamp.web.domain.content.ContentMetadataDetails;
import space.lingu.lamp.web.domain.content.ContentPublishService;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionService;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class ArticleManageController {
    private final ContentPublishProvider contentPublishProvider;
    private final ContentAccessService contentAccessService;
    private final ContentCollectionService contentCollectionService;

    public ArticleManageController(ContentPublishProvider contentPublishProvider,
                                   ContentAccessService contentAccessService,
                                   ContentCollectionService contentCollectionService) {
        this.contentPublishProvider = contentPublishProvider;
        this.contentAccessService = contentAccessService;
        this.contentCollectionService = contentCollectionService;
    }

    @GetMapping("/articles")
    public HttpResponseEntity<List<ArticleMetaVo>> getArticles(Pageable pageable) {
        Page<ContentMetadataDetails<?>> contents = contentCollectionService.getContentsRelated(
                ContentCollectionType.ARTICLES,
                0,
                pageable
        );

        return HttpResponseEntity.success(
                contents.transform(ArticleMetaVo::from)
        );
    }


    @GetMapping("/users/{userId}/articles")
    public HttpResponseEntity<List<ArticleMetaVo>> getArticlesByUser(
            @PathVariable("userId") Long userId,
            Pageable pageable) {
        Page<ContentMetadataDetails<?>> contents = contentCollectionService.getContentsRelated(
                ContentCollectionType.USER_ARTICLES,
                userId,
                pageable
        );

        return HttpResponseEntity.success(
                contents.transform(ArticleMetaVo::from)
        );
    }


    @GetMapping("/users/{userId}/articles/{articleId}")
    public HttpResponseEntity<ArticleMetaVo> getArticle(
            @PathVariable("userId") Long userId,
            @PathVariable("articleId") Long articleId) {
        ContentMetadataDetails<?> contentMetadataDetails =
                contentAccessService.getContentMetadataDetails(
                        articleId,
                        ContentType.ARTICLE
                );
        if (contentMetadataDetails.getUserId() != userId) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return HttpResponseEntity.success(
                ArticleMetaVo.from(contentMetadataDetails)
        );
    }

}
