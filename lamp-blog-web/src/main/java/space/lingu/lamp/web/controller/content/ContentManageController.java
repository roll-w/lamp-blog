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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.controller.AdminApi;
import space.lingu.lamp.web.controller.comment.vo.CommentVo;
import space.lingu.lamp.web.controller.content.vo.ContentVo;
import space.lingu.lamp.web.controller.content.vo.UrlContentType;
import space.lingu.lamp.web.domain.article.vo.ArticleVo;
import space.lingu.lamp.web.domain.content.ContentAccessService;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentIdentity;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionProviderFactory;
import space.lingu.lamp.web.domain.content.common.ContentErrorCode;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.SystemResourceOperatorProvider;

/**
 * @author RollW
 */
@AdminApi
public class ContentManageController {
    private final ContentAccessService contentAccessService;
    private final SystemResourceOperatorProvider<Long> systemResourceOperatorProvider;
    private final ContentCollectionProviderFactory contentCollectionProviderFactory;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;

    public ContentManageController(ContentAccessService contentAccessService,
                                   SystemResourceOperatorProvider<Long> systemResourceOperatorProvider,
                                   ContentCollectionProviderFactory contentCollectionProviderFactory,
                                   ContextThreadAware<ApiContext> apiContextThreadAware) {
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
        ContentDetails details = contentAccessService.getContentDetails(
                ContentIdentity.of(contentId, contentType.getContentType())
        );
        if (details.getUserId() != userId) {
            return HttpResponseEntity.of(ContentErrorCode.ERROR_CONTENT_NOT_FOUND);
        }
        return HttpResponseEntity.success(contentVoConvert(details));
    }

    private ContentVo contentVoConvert(ContentDetails details) {
        return switch (details.getContentType()) {
            case ARTICLE -> ArticleVo.of(details);
            case COMMENT -> CommentVo.of(details);
            default -> throw new IllegalStateException("Unexpected value: " + details.getContentType());
        };
    }
}
