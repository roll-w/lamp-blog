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

package space.lingu.lamp.web.domain.article.dto;

import space.lingu.NonNull;
import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentDetails;
import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.light.DataColumn;

/**
 * @author RollW
 */
public record ArticleInfo(
        @DataColumn(name = "id")
        long id,

        @DataColumn(name = "user_id")
        long userId,

        @DataColumn(name = "title")
        String title,

        @DataColumn(name = "cover")
        String cover,

        @DataColumn(name = "create_time")
        long createTime,

        @DataColumn(name = "update_time")
        long updateTime
) implements Content {

    public static ArticleInfo from(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleInfo(
                article.getId(),
                article.getUserId(),
                article.getTitle(),
                article.getCover(),
                article.getCreateTime(),
                article.getUpdateTime()
        );
    }

    public static ArticleInfo from(ContentDetails contentDetails) {
        if (contentDetails == null) {
            return null;
        }
        if (contentDetails instanceof Article article) {
            return from(article);
        }
        return null;
    }

    @NonNull
    @Override
    public String getContentId() {
        return String.valueOf(id);
    }

    @NonNull
    @Override
    public ContentType getContentType() {
        return ContentType.ARTICLE;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
