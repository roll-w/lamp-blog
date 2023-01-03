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

package space.lingu.lamp.web.service;

import com.google.common.base.Strings;
import org.springframework.stereotype.Service;
import space.lingu.Nullable;
import space.lingu.lamp.event.EventCallback;
import space.lingu.lamp.event.EventRegistry;
import space.lingu.lamp.web.data.SettingLoader;
import space.lingu.lamp.web.data.SettingProvider;
import space.lingu.lamp.web.data.database.repository.SystemSettingRepository;
import space.lingu.lamp.web.data.entity.SystemSetting;

/**
 * @author RollW
 */
@Service
public class SystemSettingService implements SettingProvider, SettingLoader,
        EventRegistry<SystemSetting, String> {
    private final SystemSettingRepository systemSettingRepository;

    public SystemSettingService(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }

    @Nullable
    @Override
    public SystemSetting getSetting(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return null;
        }
        return systemSettingRepository.getSystemSetting(key);
    }

    @Nullable
    @Override
    public String getSettingValue(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return null;
        }
        return systemSettingRepository.get(key);
    }

    @Override
    public void setSetting(String key, String value) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        systemSettingRepository.set(key, value);
    }

    @Override
    public void setSetting(SystemSetting setting) {
        if (setting == null || Strings.isNullOrEmpty(setting.getKey())) {
            return;
        }

        systemSettingRepository.set(setting);
    }

    @Override
    public void deleteSetting(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }

        systemSettingRepository.delete(key);
    }

    @Override
    public void register(EventCallback<SystemSetting> eventCallback,
                         String messagePattern) {
        // TODO: callback register
    }

    // NOTE: callback method calls needs to be async.
}
