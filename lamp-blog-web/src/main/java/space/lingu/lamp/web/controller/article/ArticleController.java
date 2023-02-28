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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.data.page.PageRequest;
import space.lingu.lamp.web.common.ApiContextHolder;
import space.lingu.lamp.web.controller.WithAdminApi;
import space.lingu.lamp.web.domain.article.dto.ArticleDetailsMetadata;
import space.lingu.lamp.web.domain.article.dto.ArticleInfo;
import space.lingu.lamp.web.domain.article.dto.ArticleRequest;
import space.lingu.lamp.web.domain.article.vo.ArticleVo;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentAccessCredentials;
import space.lingu.lamp.web.domain.content.ContentAccessService;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentMetadataDetails;
import space.lingu.lamp.web.domain.content.ContentPublishService;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.UncreatedContent;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionService;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.user.dto.UserInfo;

import java.util.List;

/**
 * @author RollW
 */
@WithAdminApi
public class ArticleController {
    // TODO: refactor article controller
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

    @GetMapping("/{userId}/articles")
    public HttpResponseEntity<List<ArticleVo>> getArticles(
            @PathVariable String userId, PageRequest pageRequest) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        // TODO: check if the user is the same as the current user

        List<ContentMetadataDetails<?>> contents = contentCollectionService.getContentsRelated(
                ContentCollectionType.USER_ARTICLES,
                userId,
                pageRequest.getPage(),
                pageRequest.getSize()
        );

        return HttpResponseEntity.success(
                contents.stream()
                        .map(ArticleVo::from)
                        .toList()
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

    @PutMapping("/{userId}/article/{articleId}")
    public HttpResponseEntity<ArticleInfo> updateArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId,
            @RequestBody ArticleRequest articleRequest) {
        return HttpResponseEntity.success();
    }


    @DeleteMapping("/{userId}/article/{articleId}")
    public HttpResponseEntity<Void> deleteArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId) {
        return HttpResponseEntity.success();
    }

    @GetMapping("/{userId}/article/{articleId}")
    public HttpResponseEntity<ArticleVo> getArticle(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "articleId") Long articleId) {
        ApiContextHolder.ApiContext apiContext = ApiContextHolder.getContext();
        UserInfo userInfo = apiContext.userInfo();
        if (apiContext.isAdminPass()) {
            ContentDetails details = contentAccessService.getContentDetails(
                    Long.toString(articleId),
                    ContentType.ARTICLE
            );
            return HttpResponseEntity.success(ArticleVo.from(details));
        }
        ContentDetails details = contentAccessService.openContent(
                Long.toString(articleId),
                ContentType.ARTICLE,
                ContentAccessCredentials.of(ContentAccessAuthType.USER, userInfo)
        );
        if (details.getUserId() != userId) {
            return HttpResponseEntity.of(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }

        return HttpResponseEntity.success(ArticleVo.from(details));
    }
}
