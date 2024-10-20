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

package space.lingu.lamp.content.collection;

/**
 * @author RollW
 */
public record SimpleContentCollectionIdentity(
        long contentCollectionId,
        ContentCollectionType contentCollectionType
) implements ContentCollectionIdentity {
    @Override
    public long getContentCollectionId() {
        return contentCollectionId;
    }

    @Override
    public ContentCollectionType getContentCollectionType() {
        return contentCollectionType;
    }

    public static SimpleContentCollectionIdentity of(
            long contentCollectionId,
            ContentCollectionType contentCollectionType) {
        return new SimpleContentCollectionIdentity(
                contentCollectionId,
                contentCollectionType);
    }
}
