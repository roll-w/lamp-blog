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

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author RollW
 */
public interface TimeAttributed {
    @Deprecated
    // TODO: use LocalDateTime instead of long
    long getCreateTime();

    @Deprecated
    long getUpdateTime();

    // TODO: remove methods below when migration is done
    default LocalDateTime getCreateDateTime() {
        return LocalDateTime.ofEpochSecond(getCreateTime(), 0, ZoneOffset.UTC);
    }

    default LocalDateTime getUpdateDateTime() {
        return LocalDateTime.ofEpochSecond(getUpdateTime(), 0, ZoneOffset.UTC);
    }

    LocalDateTime NONE = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
}
