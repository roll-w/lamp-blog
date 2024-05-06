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

package space.lingu.lamp.web.controller.content.vo;

import space.lingu.lamp.web.domain.content.ContentType;
import space.lingu.lamp.web.domain.content.collection.ContentCollectionType;

/**
 * @author RollW
 */
public enum UrlContentType implements ContentType.Type {
    ARTICLE("articles", ContentType.ARTICLE, ContentCollectionType.USER_ARTICLES),
    COMMENT("comments", ContentType.COMMENT, ContentCollectionType.USER_COMMENTS),
    POST("posts", ContentType.POST, null),
    IMAGE("images", ContentType.IMAGE, null),
    ;

    private final String url;
    private final ContentType contentType;
    // TODO: not graceful
    private final ContentCollectionType userCollectionType;

    UrlContentType(String url, ContentType contentType,
                   ContentCollectionType userCollectionType) {
        this.url = url;
        this.contentType = contentType;
        this.userCollectionType = userCollectionType;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public ContentType getContentType() {
        return contentType;
    }

    public ContentCollectionType getUserCollectionType() {
        return userCollectionType;
    }

    public static UrlContentType fromUrl(String url) {
        for (UrlContentType value : values()) {
            if (value.getUrl().equalsIgnoreCase(url)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown url: " + url);
    }
}
