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

import space.lingu.lamp.content.ContentAccessAuthType;
import space.lingu.lamp.content.ContentMetadataDetails;
import space.lingu.lamp.content.ContentStatus;
import space.lingu.lamp.content.article.Article;

import java.time.OffsetDateTime;

/**
 * @author RollW
 */
public record ArticleMetaVo(
        long id,
        String title,
        String content,
        long authorId,
        OffsetDateTime createTime,
        OffsetDateTime updateTime,
        ContentAccessAuthType accessAuthType,
        ContentStatus contentStatus
) {
    public static ArticleMetaVo from(ContentMetadataDetails<?> contentMetadataDetails) {
        if (!(contentMetadataDetails.getContentDetails() instanceof Article article)) {
            return null;
        }
        String content = article.getContent();
        return new ArticleMetaVo(
                article.getId(),
                article.getTitle(),
                content,
                article.getUserId(),
                article.getCreateTime(),
                article.getUpdateTime(),
                contentMetadataDetails.getContentAccessAuthType(),
                contentMetadataDetails.getContentStatus()
        );
    }

}
