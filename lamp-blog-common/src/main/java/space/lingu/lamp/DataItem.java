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

package space.lingu.lamp;

import tech.rollw.common.web.system.SystemResource;

import java.io.Serializable;

/**
 * Indicates a database entity.
 *
 * @author RollW
 */
public interface DataItem<T extends DataItem<T>> extends Serializable, SystemResource<Long> {
    /**
     * Get the id of the entity.
     *
     * @return the id of the entity.
     */
    Long getId();

    @Override
    default Long getResourceId() {
        return getId();
    }

    long getCreateTime();

    long getUpdateTime();

    EntityBuilder<T> toBuilder();
}
