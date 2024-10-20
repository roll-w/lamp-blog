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

import space.lingu.lamp.web.domain.article.dto.ArticleDetailsMetadata;
import space.lingu.lamp.content.SimpleUncreatedContent;
import space.lingu.lamp.content.ContentType;
import space.lingu.lamp.content.UncreatedContent;

/**
 * @author RollW
 */
public record ArticleCreateRequest(
        String title,
        String content
) {
    public UncreatedContent toUncreatedContent(long userId,
                                               ArticleDetailsMetadata articleDetailsMetadata) {
        return new SimpleUncreatedContent(
                ContentType.ARTICLE,
                userId,
                title, content,
                articleDetailsMetadata
        );
    }
}
