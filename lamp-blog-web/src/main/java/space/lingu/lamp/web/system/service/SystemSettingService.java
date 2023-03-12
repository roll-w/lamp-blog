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

package space.lingu.lamp.web.system.service;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.data.page.Offset;
import space.lingu.lamp.data.page.PageHelper;
import space.lingu.lamp.event.EventCallback;
import space.lingu.lamp.event.EventRegistry;
import space.lingu.lamp.web.common.CacheNames;
import space.lingu.lamp.web.system.repository.SystemSettingRepository;
import space.lingu.lamp.web.system.setting.SystemSetting;
import space.lingu.lamp.web.system.setting.SettingLoader;
import space.lingu.lamp.web.system.setting.SettingProvider;

import java.util.List;
import java.util.Set;

/**
 * @author RollW
 */
@Service
public class SystemSettingService implements SettingProvider, SettingLoader,
        EventRegistry<SystemSetting, String> {

    public static final String ALL_SETTINGS = "AllSettingsCallback";
    private final SystemSettingRepository systemSettingRepository;
    private final Cache cache;

    public SystemSettingService(SystemSettingRepository systemSettingRepository,
                                CacheManager cacheManager) {
        this.systemSettingRepository = systemSettingRepository;
        this.cache = cacheManager.getCache(CacheNames.SETTING);
    }
    // TODO: setting cache

    @Nullable
    @Override
    public SystemSetting getSetting(@NonNull String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new IllegalStateException("Key is null or empty.");
        }
        Cache.ValueWrapper old = cache.get(key);
        if (old != null) {
            return (SystemSetting) old.get();
        }
        SystemSetting setting = systemSettingRepository.getSystemSetting(key);
        cache.put(key, setting);
        return setting;
    }

    @Nullable
    @Override
    public SystemSetting getSetting(@NonNull String key, String defaultValue) {
        if (Strings.isNullOrEmpty(key)) {
            throw new IllegalStateException("Key is null or empty.");
        }
        Cache.ValueWrapper old = cache.get(key);
        if (old != null) {
            return tryGetSystemSetting((SystemSetting) old.get(), key, defaultValue);
        }
        SystemSetting setting = systemSettingRepository.getSystemSetting(key);
        cache.put(key, setting);
        return tryGetSystemSetting(setting, key, defaultValue);
    }

    @Nullable
    @Override
    public String getSettingValue(@NonNull String key) {
        return getSettingValue(key, null);
    }

    @Nullable
    @Override
    public String getSettingValue(@NonNull String key, String defaultValue) {
        if (Strings.isNullOrEmpty(key)) {
            throw new IllegalStateException("Key is null or empty.");
        }
        Cache.ValueWrapper old = cache.get(key);
        if (old != null) {
            return tryGetValue((SystemSetting) old.get(), defaultValue);
        }
        SystemSetting setting = systemSettingRepository.getSystemSetting(key);
        cache.put(key, setting);
        return tryGetValue(setting, defaultValue);
    }

    @Override
    public List<SystemSetting> getSettings(int page, int size) {
        Offset offset = PageHelper.offset(page, size);
        return systemSettingRepository.get(offset.offset(), offset.limit());
    }

    private String tryGetValue(SystemSetting setting, String defaultValue) {
        if (setting == null || setting.getValue() == null) {
            return defaultValue;
        }
        return setting.getValue();
    }

    private SystemSetting tryGetSystemSetting(SystemSetting setting, String key,
                                              String defaultValue) {
        if (setting == null || setting.getValue() == null) {
            return new SystemSetting(key, defaultValue);
        }
        return setting;
    }


    @Override
    public void setSetting(String key, String value) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        SystemSetting setting = new SystemSetting(key, value);
        invokeCallback(setting);
        cache.put(key, setting);
        systemSettingRepository.set(setting);
    }

    @Override
    public void setSetting(SystemSetting setting) {
        if (setting == null || Strings.isNullOrEmpty(setting.getKey())) {
            return;
        }
        invokeCallback(setting);
        cache.put(setting.getKey(), setting);
        systemSettingRepository.set(setting);
    }

    @Override
    public void deleteSetting(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        SystemSetting change = new SystemSetting(key, null);
        cache.evictIfPresent(key);
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
