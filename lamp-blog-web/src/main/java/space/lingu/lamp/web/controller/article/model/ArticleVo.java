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

package space.lingu.lamp.web.controller.article.model;

import space.lingu.lamp.web.controller.content.vo.ContentVo;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentMetadataDetails;

/**
 * @author RollW
 */
public record ArticleVo(
        long id,
        String title,
        String content,
        long userId,
        long createTime,
        long updateTime,
        ContentAccessAuthType accessAuthType
        // TODO: add more fields
) implements ContentVo {

    public static ArticleVo of(ContentMetadataDetails<?> contentMetadataDetails) {
        if (!(contentMetadataDetails.getContentDetails() instanceof Article article)) {
            return null;
        }
        return new ArticleVo(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getUserId(),
                article.getCreateTime(),
                article.getUpdateTime(),
                contentMetadataDetails.getContentAccessAuthType()
        );
    }

    public static ArticleVo of(ContentDetails contentDetails) {
        if (contentDetails instanceof ContentMetadataDetails<?> contentMetadataDetails) {
            return of(contentMetadataDetails);
        }
        if (!(contentDetails instanceof Article article)) {
            return null;
        }
        return new ArticleVo(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getUserId(),
                article.getCreateTime(),
                article.getUpdateTime(),
                ContentAccessAuthType.PUBLIC
        );
    }

    public static ArticleVo of(Content content) {
        if (content instanceof ContentDetails contentDetails) {
            return of(contentDetails);
        }
        return null;
    }
}
