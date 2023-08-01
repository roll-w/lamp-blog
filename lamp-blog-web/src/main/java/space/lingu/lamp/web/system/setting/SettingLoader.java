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

package space.lingu.lamp.web.system.setting;

import space.lingu.NonNull;
import space.lingu.Nullable;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
public interface SettingLoader {
    @Nullable
    SystemSetting getSetting(@NonNull String key);

    /**
     * Get the setting value by key, if the setting not found, return
     * the default value, if the setting value is null, return null.
     * <p>
     * Compare to the {@link #getSettingValue(String, String)} method,
     * this method will not return the default value when the setting
     * value is null.
     *
     * @param defaultValue when the key is not found, return this value,
     *                     but if the setting value is null, default will not
     *                     be used
     */
    @Nullable
    SystemSetting getSetting(@NonNull String key, String defaultValue);

    @Nullable
    String getSettingValue(@NonNull String key);

    /**
     * Get the setting value by key, if the setting not found, return
     * the default value, if the setting value is null, return the default
     * value.
     *
     * @see #getSetting(String, String)
     */
    @Nullable
    String getSettingValue(@NonNull String key, String defaultValue);

    List<SystemSetting> getSettings(Pageable pageable);
}
