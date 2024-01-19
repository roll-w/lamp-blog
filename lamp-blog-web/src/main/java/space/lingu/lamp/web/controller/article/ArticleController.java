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

import org.springframework.web.bind.annotation.*;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.controller.Api;
import space.lingu.lamp.web.domain.article.dto.ArticleDetailsMetadata;
import space.lingu.lamp.web.domain.article.dto.ArticleInfo;
import space.lingu.lamp.web.domain.article.dto.ArticleRequest;
import space.lingu.lamp.web.domain.article.vo.ArticleVo;
import space.lingu.lamp.web.domain.content.*;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionService;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;

import java.util.List;

/**
 * @author RollW
 */
@Api
public class ArticleController {
    // TODO: refactor article controller,
    //  splits admin and common users into two controllers
    private final ContentPublishProvider contentPublishProvider;
    private final ContentAccessService contentAccessService;
    private final ContentCollectionService contentCollectionService;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;

    public ArticleController(ContentPublishProvider contentPublishProvider,
                             ContentAccessService contentAccessService,
                             ContentCollectionService contentCollectionService,
                             ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.contentPublishProvider = contentPublishProvider;
        this.contentAccessService = contentAccessService;
        this.contentCollectionService = contentCollectionService;
        this.apiContextThreadAware = apiContextThreadAware;
    }


    @GetMapping("/users/{userId}/articles")
    public HttpResponseEntity<List<ArticleVo>> getArticles(
            @PathVariable Long userId, Pageable pageRequest) {
        ContextThread<ApiContext> apiContextThread =
                apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        // TODO: check if the user is the same as the current user
        ContentAccessCredentials contentAccessCredentials =
                ContentAccessCredentials.of(
                        ContentAccessAuthType.USER,
                        apiContext.getUser() == null ? null
                                : apiContext.getUser().getUserId()
                );

        Page<ContentMetadataDetails<?>> contents = contentCollectionService.accessContentsRelated(
                ContentCollectionType.USER_ARTICLES,
                userId,
                contentAccessCredentials,
                pageRequest
        );
        return HttpResponseEntity.success(
                contents.transform(ArticleVo::from)
        );
    }

    @PostMapping("/articles")
    public HttpResponseEntity<ArticleInfo> createArticle(
            @RequestBody ArticleRequest articleRequest) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        if (!apiContext.hasUser()) {
            throw new ContentException(UserErrorCode.ERROR_USER_NOT_LOGIN);
        }
        long userId = apiContext.getUser().getUserId();
        UncreatedContent uncreatedContent = articleRequest.toUncreatedContent(
                userId,
                ArticleDetailsMetadata.EMPTY
        );
        ContentDetails articleDetails =
                contentPublishProvider.publishContent(uncreatedContent);
        ArticleInfo articleInfo = ArticleInfo.from(articleDetails);
        return HttpResponseEntity.success(articleInfo);
    }

    @PutMapping("/users/{userId}/articles/{articleId}")
    public HttpResponseEntity<ArticleInfo> updateArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId,
            @RequestBody ArticleRequest articleRequest) {
        return HttpResponseEntity.success();
    }


    @DeleteMapping("/users/{userId}/articles/{articleId}")
    public HttpResponseEntity<Void> deleteArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId) {
        return HttpResponseEntity.success();
    }

    @GetMapping("/users/{userId}/articles/{articleId}")
    public HttpResponseEntity<ArticleVo> getArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        ContentDetails details = contentAccessService.openContent(
                articleId,
                ContentType.ARTICLE,
                ContentAccessCredentials.of(ContentAccessAuthType.USER, apiContext.getUser())
        );
        if (details.getUserId() != userId) {
            return HttpResponseEntity.of(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }

        return HttpResponseEntity.success(ArticleVo.from(details));
    }
}
