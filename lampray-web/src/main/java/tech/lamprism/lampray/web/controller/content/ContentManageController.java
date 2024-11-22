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

package tech.lamprism.lampray.web.controller.content;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tech.lamprism.lampray.web.common.ApiContext;
import tech.lamprism.lampray.web.controller.AdminApi;
import tech.lamprism.lampray.web.controller.comment.model.CommentVo;
import tech.lamprism.lampray.web.controller.content.vo.ContentVo;
import tech.lamprism.lampray.web.controller.content.vo.UrlContentType;
import tech.lamprism.lampray.web.controller.article.model.ArticleVo;
import tech.lamprism.lampray.content.ContentAccessService;
import tech.lamprism.lampray.content.ContentDetails;
import tech.lamprism.lampray.content.ContentIdentity;
import tech.lamprism.lampray.content.collection.ContentCollectionProviderFactory;
import tech.lamprism.lampray.content.common.ContentErrorCode;
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
