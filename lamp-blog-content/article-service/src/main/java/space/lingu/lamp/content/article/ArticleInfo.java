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

package space.lingu.lamp.content.article;

import space.lingu.NonNull;
import space.lingu.lamp.content.Content;
import space.lingu.lamp.content.ContentDetails;
import space.lingu.lamp.content.ContentDetailsMetadata;
import space.lingu.lamp.content.ContentType;

import java.time.LocalDateTime;

/**
 * @author RollW
 */
public record ArticleInfo(
        long id,
        long userId,
        String title,
        String cover,
        LocalDateTime createTime,
        LocalDateTime updateTime
) implements Content {
    public static ArticleInfo from(ContentDetails contentDetails) {
        if (contentDetails == null) {
            return null;
        }
        ContentDetailsMetadata metadata = contentDetails.getMetadata();
        if (!(metadata instanceof ArticleDetailsMetadata articleMetadata)) {
            return new ArticleInfo(
                    contentDetails.getContentId(),
                    contentDetails.getUserId(),
                    contentDetails.getTitle(),
                    null,
                    contentDetails.getCreateDateTime(),
                    contentDetails.getUpdateDateTime()
            );
        }
        return new ArticleInfo(
                contentDetails.getContentId(),
                contentDetails.getUserId(),
                contentDetails.getTitle(),
                articleMetadata.getCover(),
                contentDetails.getCreateDateTime(),
                contentDetails.getUpdateDateTime()
        );
    }

    @Override
    public long getContentId() {
        return id;
    }

    @NonNull
    @Override
    public ContentType getContentType() {
        return ContentType.ARTICLE;
    }

    @Override
    public long getUserId() {
        return userId;
    }
}
