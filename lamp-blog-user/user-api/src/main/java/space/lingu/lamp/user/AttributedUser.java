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

package space.lingu.lamp.user;

import space.lingu.NonNull;
import space.lingu.lamp.TimeAttributed;

import java.time.LocalDateTime;

/**
 * @author RollW
 */
public interface AttributedUser extends UserIdentity, TimeAttributed {
    boolean isEnabled();

    boolean isLocked();

    boolean isCanceled();

    @NonNull
    @Override
    LocalDateTime getCreateTime();

    @NonNull
    @Override
    LocalDateTime getUpdateTime();

    default boolean isNormal() {
        return isEnabled() && !isLocked() && !isCanceled();
    }
}
