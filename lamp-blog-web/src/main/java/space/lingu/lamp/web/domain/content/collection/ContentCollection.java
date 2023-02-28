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

import space.lingu.lamp.web.domain.content.Content;

import java.util.List;

/**
 * @author RollW
 */
public class ContentCollection {
    private final ContentCollectionType contentCollectionType;
    private final String contentCollectionId;
    private final List<? extends Content> contentCollection;

    public ContentCollection(ContentCollectionType contentCollectionType,
                             String contentCollectionId,
                             List<? extends Content> contentCollection) {
        this.contentCollectionType = contentCollectionType;
        this.contentCollectionId = contentCollectionId;
        this.contentCollection = contentCollection;
    }
}
