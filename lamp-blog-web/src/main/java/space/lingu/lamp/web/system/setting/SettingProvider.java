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
public interface SettingProvider extends SettingLoader {
    @Nullable
    @Override
    SystemSetting getSetting(@NonNull String key);

    @Nullable
    @Override
    SystemSetting getSetting(@NonNull String key, String defaultValue);

    @Nullable
    @Override
    String getSettingValue(@NonNull String key);

    @Nullable
    @Override
    String getSettingValue(@NonNull String key, String defaultValue);

    @Override
    List<SystemSetting> getSettings(Pageable pageable);

    void setSetting(String key, String value);

    void setSetting(SystemSetting setting);

    void deleteSetting(String key);
}
