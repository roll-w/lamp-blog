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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.controller.Api;
import space.lingu.lamp.web.controller.article.model.ArticleCreateRequest;
import space.lingu.lamp.web.domain.article.dto.ArticleDetailsMetadata;
import space.lingu.lamp.web.domain.article.dto.ArticleInfo;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentPublishProvider;
import space.lingu.lamp.content.UncreatedContent;
import space.lingu.lamp.content.common.ContentException;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;

/**
 * @author RollW
 */
@Api
public class ArticleController {
    private final ContentPublishProvider contentPublishProvider;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;

    public ArticleController(ContentPublishProvider contentPublishProvider,
                             ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.contentPublishProvider = contentPublishProvider;
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @PostMapping("/articles")
    public HttpResponseEntity<ArticleInfo> createArticle(
            @RequestBody ArticleCreateRequest articleCreateRequest) {
        ContextThread<ApiContext> apiContextThread = apiContextThreadAware.getContextThread();
        ApiContext apiContext = apiContextThread.getContext();
        if (!apiContext.hasUser()) {
            throw new ContentException(UserErrorCode.ERROR_USER_NOT_LOGIN);
        }
        long userId = apiContext.getUser().getUserId();
        UncreatedContent uncreatedContent = articleCreateRequest.toUncreatedContent(
                userId,
                ArticleDetailsMetadata.EMPTY
        );
        ContentDetails articleDetails =
                contentPublishProvider.publishContent(uncreatedContent);
        ArticleInfo articleInfo = ArticleInfo.from(articleDetails);
        return HttpResponseEntity.success(articleInfo);
    }
}
