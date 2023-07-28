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

/**
 * Basic infos of content.
 *
 * @author RollW
 */
public interface Content extends ContentIdentity {
    int INVALID_VERSION = -1;

    /**
     * Get the id of the user who created the content.
     *
     * @return user id
     */
    long getUserId();

    /**
     * Get the id of the content.
     *
     * @return content id
     */
    @Override
    long getContentId();

    /**
     * Get the type of the content.
     *
     * @return content type
     */
    @NonNull
    @Override
    ContentType getContentType();

    // TODO: support history version
    default int getVersion() {
        return INVALID_VERSION;
    }
}
