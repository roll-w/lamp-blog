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

package space.lingu.lamp.web.domain.content.collection;

import space.lingu.lamp.web.domain.content.ContentType;

/**
 * @author RollW
 */
public enum ContentCollectionType {
    /**
     * All articles.
     */
    ARTICLES(ContentType.ARTICLE),

    /**
     * User's articles.
     */
    USER_ARTICLES(ContentType.ARTICLE),

    /**
     * All comments.
     */
    COMMENTS(ContentType.COMMENT),

    /**
     * Article comments.
     */
    ARTICLE_COMMENTS(ContentType.COMMENT),

    /**
     * Post comments.
     */
    POST_COMMENTS(ContentType.COMMENT),

    /**
     * User's comments.
     */
    USER_COMMENTS(ContentType.COMMENT),

    ;

    /**
     * The content type of the collection.
     */
    private final ContentType contentType;

    ContentCollectionType(ContentType contentType) {
        this.contentType = contentType;
    }

    public ContentType getContentType() {
        return contentType;
    }
}
