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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.web.LampBlogSystemApplication;
import space.lingu.lamp.web.system.repository.SystemSettingRepository;
import space.lingu.lamp.web.system.setting.LocalSettingLoader;
import space.lingu.lamp.web.system.setting.SettingLoader;
import space.lingu.lamp.web.system.setting.SettingProvider;
import space.lingu.lamp.web.system.setting.SystemSetting;
import tech.rollw.common.event.EventCallback;
import tech.rollw.common.event.EventRegistry;
import tech.rollw.common.web.page.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @author RollW
 */
@Service
public class SystemSettingService implements SettingProvider, SettingLoader,
        EventRegistry<SystemSetting, String> {
    private static final Logger logger = LoggerFactory.getLogger(SystemSettingService.class);

    public static final String ALL_SETTINGS = "AllSettingsCallback";
    private final LocalSettingLoader localSettingLoader;
    private final SystemSettingRepository systemSettingRepository;

    public SystemSettingService(SystemSettingRepository systemSettingRepository)
            throws IOException {
        this.systemSettingRepository = systemSettingRepository;
        this.localSettingLoader =
                LocalSettingLoader.load(LampBlogSystemApplication.class);
    }

    @Nullable
    @Override
    public SystemSetting getSetting(@NonNull String key) {
        if (Strings.isNullOrEmpty(key)) {
            throw new IllegalStateException("Key is null or empty.");
        }
        SystemSetting dbSetting = systemSettingRepository.getById(key);
        if (dbSetting != null) {
            return dbSetting;
        }
        return localSettingLoader.getSetting(key);
    }

    @Nullable
    @Override
    public SystemSetting getSetting(@NonNull String key, String defaultValue) {
        if (Strings.isNullOrEmpty(key)) {
            throw new IllegalStateException("Key is null or empty.");
        }
        SystemSetting dbSetting = systemSettingRepository.getById(key);
        if (dbSetting != null) {
            return dbSetting;
        }
        return localSettingLoader.getSetting(key, defaultValue);
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
        SystemSetting dbSetting = systemSettingRepository.getById(key);
        if (dbSetting != null) {
            return dbSetting.getValue() == null
                    ? defaultValue
                    : dbSetting.getValue();
        }
        return localSettingLoader.getSettingValue(key, defaultValue);
    }

    @Override
    public List<SystemSetting> getSettings(Pageable pageable) {
        return systemSettingRepository.get(pageable.toOffset());
    }


    @Override
    public void setSetting(String key, String value) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        SystemSetting setting = new SystemSetting(key, value);
        invokeCallback(setting);
        systemSettingRepository.update(setting);
    }

    @Override
    public void setSetting(SystemSetting setting) {
        if (setting == null || Strings.isNullOrEmpty(setting.getKey())) {
            return;
        }
        invokeCallback(setting);
        systemSettingRepository.update(setting);
    }

    @Override
    public void deleteSetting(String key) {
        if (Strings.isNullOrEmpty(key)) {
            return;
        }
        SystemSetting change = new SystemSetting(key, null);
        invokeCallback(change);
        systemSettingRepository.deleteById(key);
    }

    @Override
    public void register(EventCallback<SystemSetting> eventCallback,
                         String messagePattern) {
        Callback callback = new Callback(eventCallback, messagePattern);
        callbacks.add(callback);
    }

    private SystemSetting loadLocalSetting(String key) {
        return localSettingLoader.getSetting(key);
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
