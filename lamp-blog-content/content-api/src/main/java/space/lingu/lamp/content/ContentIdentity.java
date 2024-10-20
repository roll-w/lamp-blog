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

package space.lingu.lamp.content;

import space.lingu.NonNull;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceKind;

import java.io.Serializable;

/**
 * @author RollW
 */
public interface ContentIdentity extends ContentTrait, SystemResource<Long>, Serializable {
    @Override
    long getContentId();

    @Override
    ContentType getContentType();

    @Override
    default Long getResourceId() {
        return getContentId();
    }

    @NonNull
    @Override
    default SystemResourceKind getSystemResourceKind() {
        return getContentType().getSystemResourceKind();
    }

    static ContentIdentity of(long contentId, @NonNull ContentType contentType) {
        return new SimpleContentIdentity(contentId, contentType);
    }
}
