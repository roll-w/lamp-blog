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

import space.lingu.lamp.web.domain.article.Article;
import space.lingu.lamp.web.domain.article.ArticleStatus;
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

        @DataColumn(name = "status")
        ArticleStatus status,

        @DataColumn(name = "create_time")
        long createTime,

        @DataColumn(name = "update_time")
        long updateTime
) {

    public static ArticleInfo from(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleInfo(
                article.getId(),
                article.getUserId(),
                article.getTitle(),
                article.getCover(),
                article.getStatus(),
                article.getCreateTime(),
                article.getUpdateTime()
        );
    }
}
