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

package space.lingu.lamp.web.controller.content;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.controller.Api;
import space.lingu.lamp.web.controller.comment.vo.CommentVo;
import space.lingu.lamp.web.controller.content.vo.ContentVo;
import space.lingu.lamp.web.controller.content.vo.UrlContentType;
import space.lingu.lamp.web.domain.article.vo.ArticleVo;
import space.lingu.lamp.web.domain.content.*;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionIdentity;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionProviderFactory;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import space.lingu.lamp.web.domain.content.common.ContentException;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.SimpleSystemResource;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceOperatorProvider;

import java.util.List;

/**
 * @author RollW
 */
@Api
public class ContentController {
    private final ContentPublishProvider contentPublishProvider;
    private final ContentAccessService contentAccessService;
    private final SystemResourceOperatorProvider<Long> systemResourceOperatorProvider;
    private final ContentCollectionProviderFactory contentCollectionProviderFactory;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;

    public ContentController(ContentPublishProvider contentPublishProvider,
                             ContentAccessService contentAccessService,
                             SystemResourceOperatorProvider<Long> systemResourceOperatorProvider,
                             ContentCollectionProviderFactory contentCollectionProviderFactory,
                             ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.contentPublishProvider = contentPublishProvider;
        this.contentAccessService = contentAccessService;
        this.systemResourceOperatorProvider = systemResourceOperatorProvider;
        this.contentCollectionProviderFactory = contentCollectionProviderFactory;
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @GetMapping("/users/{userId}/{contentType}/{contentId}")
    public HttpResponseEntity<ContentVo> getContent(
            @PathVariable("userId") Long userId,
            @PathVariable("contentType") UrlContentType contentType,
            @PathVariable("contentId") Long contentId) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        ContentDetails details = contentAccessService.openContent(
                ContentIdentity.of(contentId, contentType.getContentType()),
                ContentAccessCredentials.of(
                        ContentAccessAuthType.USER, apiContext.getUser()
                )
        );
        if (details.getUserId() != userId) {
            return HttpResponseEntity.of(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }

        return HttpResponseEntity.success(contentVoConvert(details));
    }


    @GetMapping("/{contentType}")
    public HttpResponseEntity<Void> getContents(
            @PathVariable("contentType") UrlContentType contentType) {


        return HttpResponseEntity.success();
    }

    @GetMapping("/users/{userId}/{contentType}")
    public HttpResponseEntity<List<ContentVo>> getUserContents(
            @PathVariable("userId") Long userId,
            @PathVariable("contentType") UrlContentType contentType) {
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

        List<ContentMetadataDetails<?>> contents =
                contentCollectionProviderFactory.getContents(
                        ContentCollectionIdentity.of(
                                userId,
                                contentType.getUserCollectionType()
                        ),
                        contentAccessCredentials
                );
        return HttpResponseEntity.success(
                contents.stream().map(this::contentVoConvert).toList()
        );
    }

    @DeleteMapping("/users/{userId}/{contentType}/{contentId}")
    public HttpResponseEntity<Void> deleteContent(
            @PathVariable("userId") Long userId,
            @PathVariable("contentType") UrlContentType contentType,
            @PathVariable("contentId") Long contentId) {
        ContentOperator contentOperator =
                systemResourceOperatorProvider.getSystemResourceOperator(
                        getSystemResource(contentId, contentType), true
                ).cast(ContentOperator.class);
        if (userId != contentOperator.getUserId()) {
            throw new ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        contentOperator.enableAutoUpdate()
                .delete();
        return HttpResponseEntity.success();
    }

    @PutMapping("/users/{userId}/{contentType}/{contentId}")
    public HttpResponseEntity<Void> updateContent(
            @PathVariable("userId") Long userId,
            @PathVariable("contentType") UrlContentType contentType,
            @PathVariable("contentId") Long contentId) {
        return HttpResponseEntity.success();
    }

    private SystemResource<Long> getSystemResource(Long contentId,
                                                   UrlContentType contentType) {
        return new SimpleSystemResource<>(
                contentId,
                contentType.getContentType().getSystemResourceKind()
        );
    }

    private ContentVo contentVoConvert(ContentDetails details) {
        return switch (details.getContentType()) {
            case ARTICLE -> ArticleVo.of(details);
            case COMMENT -> CommentVo.of(details);
            default -> throw new IllegalStateException("Unexpected value: " + details.getContentType());
        };
    }
}
