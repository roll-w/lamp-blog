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
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.controller.Api;
import space.lingu.lamp.web.domain.article.dto.ArticleDetailsMetadata;
import space.lingu.lamp.web.domain.article.dto.ArticleInfo;
import space.lingu.lamp.web.domain.article.dto.ArticleRequest;
import space.lingu.lamp.web.domain.article.vo.ArticleVo;
import space.lingu.lamp.web.domain.content.*;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionService;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.user.dto.UserInfo;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
@Api
public class ArticleController {
    // TODO: refactor article controller,
    //  splits admin and common users into two controllers
    private final ContentPublishService contentPublishService;
    private final ContentAccessService contentAccessService;
    private final ContentCollectionService contentCollectionService;

    public ArticleController(ContentPublishService contentPublishService,
                             ContentAccessService contentAccessService,
                             ContentCollectionService contentCollectionService) {
        this.contentPublishService = contentPublishService;
        this.contentAccessService = contentAccessService;
        this.contentCollectionService = contentCollectionService;
    }


    @GetMapping("/user/{userId}/articles")
    public HttpResponseEntity<List<ArticleVo>> getArticles(
            @PathVariable Long userId, Pageable pageRequest) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        // TODO: check if the user is the same as the current user
        ContentAccessCredentials contentAccessCredentials =
                ContentAccessCredentials.of(ContentAccessAuthType.USER, apiContext.rawId());

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

    @PostMapping("/article")
    public HttpResponseEntity<ArticleInfo> createArticle(
            @RequestBody ArticleRequest articleRequest) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        long userId = apiContext.id();
        UncreatedContent uncreatedContent = articleRequest.toUncreatedContent(
                userId,
                ArticleDetailsMetadata.EMPTY
        );
        ContentDetails articleDetails =
                contentPublishService.publishContent(uncreatedContent);
        ArticleInfo articleInfo = ArticleInfo.from(articleDetails);
        return HttpResponseEntity.success(articleInfo);
    }

    @PutMapping("/user/{userId}/article/{articleId}")
    public HttpResponseEntity<ArticleInfo> updateArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId,
            @RequestBody ArticleRequest articleRequest) {
        return HttpResponseEntity.success();
    }


    @DeleteMapping("/user/{userId}/article/{articleId}")
    public HttpResponseEntity<Void> deleteArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId) {
        return HttpResponseEntity.success();
    }

    @GetMapping("/user/{userId}/article/{articleId}")
    public HttpResponseEntity<ArticleVo> getArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        UserInfo userInfo = apiContext.userInfo();
        ContentDetails details = contentAccessService.openContent(
                articleId,
                ContentType.ARTICLE,
                ContentAccessCredentials.of(ContentAccessAuthType.USER, userInfo)
        );
        if (details.getUserId() != userId) {
            return HttpResponseEntity.of(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }

        return HttpResponseEntity.success(ArticleVo.from(details));
    }
}
