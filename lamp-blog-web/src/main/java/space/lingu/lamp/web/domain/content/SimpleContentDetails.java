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

package space.lingu.lamp.web.domain.content;

import space.lingu.NonNull;
import space.lingu.Nullable;

/**
 * @author RollW
 */
public record SimpleContentDetails(
        long contentId,
        @NonNull ContentType contentType,
        long userId,
        @Nullable String title,
        @Nullable String content,
        long createTime,
        long updateTime,
        @Nullable ContentDetailsMetadata contentDetailsMetadata
) implements ContentDetails {
    @Override
    public long getContentId() {
        return contentId;
    }

    @Override
    @NonNull
    public ContentType getContentType() {
        return contentType;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    @Nullable
    public String getTitle() {
        return title;
    }

    @Override
    @Nullable
    public String getContent() {
        return content;
    }

    @Override
    @Nullable
    public ContentDetailsMetadata getMetadata() {
        return contentDetailsMetadata;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
    public long getUpdateTime() {
        return updateTime;
    }
}
