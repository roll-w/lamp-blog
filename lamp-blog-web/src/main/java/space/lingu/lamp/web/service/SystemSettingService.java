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
import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import space.lingu.Nullable;
import space.lingu.lamp.event.EventCallback;
import space.lingu.lamp.event.EventRegistry;
import space.lingu.lamp.web.data.SettingLoader;
import space.lingu.lamp.web.data.SettingProvider;
import space.lingu.lamp.web.data.database.repository.SystemSettingRepository;
import space.lingu.lamp.web.data.entity.SystemSetting;

import java.util.Set;

/**
 * @author RollW
 */
@Service
public class SystemSettingService implements SettingProvider, SettingLoader,
        EventRegistry<SystemSetting, String> {
    public static final String ALL_SETTINGS = "AllSettingsCallback";
    private final SystemSettingRepository systemSettingRepository;

    public SystemSettingService(SystemSettingRepository systemSettingRepository) {
        this.systemSettingRepository = systemSettingRepository;
    }
    // TODO: setting cache

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
        SystemSetting setting = new SystemSetting(key, value);
        invokeCallback(setting);
        systemSettingRepository.set(setting);
    }

    @Override
    public void setSetting(SystemSetting setting) {
        if (setting == null || Strings.isNullOrEmpty(setting.getKey())) {
            return;
        }
        invokeCallback(setting);
        systemSettingRepository.set(setting);
    }

    @Override
    public void deleteSetting(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        SystemSetting change = new SystemSetting(key, null);
        invokeCallback(change);
        systemSettingRepository.delete(key);
    }

    @Override
    public void register(EventCallback<SystemSetting> eventCallback,
                         String messagePattern) {
        Callback callback = new Callback(eventCallback, messagePattern);
        callbacks.add(callback);
    }

    // TODO: callback method calls needs to be async.
    private void invokeCallback(SystemSetting change) {
        callbacks.forEach(callback -> {
            if (checkMode(change.getKey(), callback.message)) {
                callback.callback().onEvent(change);
            }
        });
    }

    private boolean checkModes(String checker, String[] modes) {
        if (modes == null) {
            return true;
        }
        if (checker == null) {
            return false;
        }
        for (String mode : modes) {
            if (ALL_SETTINGS.equals(mode)) {
                return true;
            }
            if (checker.startsWith(mode)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMode(String checker, String mode) {
        if (mode == null) {
            return true;
        }
        if (checker == null) {
            return false;
        }
        if (ALL_SETTINGS.equals(mode)) {
            return true;
        }
        return checker.startsWith(mode);
    }


    private final Set<Callback> callbacks = Sets.newConcurrentHashSet();

    private record Callback(
            EventCallback<SystemSetting> callback,
            String message
    ) {
    }

}
