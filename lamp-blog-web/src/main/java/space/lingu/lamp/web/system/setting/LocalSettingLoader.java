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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @author RollW
 */
public class LocalSettingLoader implements SettingLoader {
    private final Properties properties;

    public LocalSettingLoader(InputStream inputStream) throws IOException {
        this.properties = new Properties();
        if (inputStream != null) {
            properties.load(inputStream);
        }
    }

    @Nullable
    @Override
    public SystemSetting getSetting(@NonNull String key) {
        if (properties.containsKey(key)) {
            return new SystemSetting(key, properties.getProperty(key));
        }
        return null;
    }

    @Nullable
    @Override
    public SystemSetting getSetting(@NonNull String key,
                                    String defaultValue) {
        if (properties.containsKey(key)) {
            return new SystemSetting(key, properties.getProperty(key));
        }
        return new SystemSetting(key, defaultValue);
    }

    @Nullable
    @Override
    public String getSettingValue(@NonNull String key) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key);
        }
        return null;
    }

    @Nullable
    @Override
    public String getSettingValue(@NonNull String key,
                                  String defaultValue) {
        if (properties.containsKey(key)) {
            String value = properties.getProperty(key);
            return value == null ? defaultValue : value;
        }
        return defaultValue;
    }

    @Override
    public List<SystemSetting> getSettings(Pageable pageable) {
        int skip = pageable.getSize() * (pageable.getPage() - 1);
        int limit = pageable.getSize();

        return properties.entrySet()
                .stream()
                .map(entry -> new SystemSetting(
                        entry.getKey().toString(),
                        entry.getValue().toString()))
                .skip(skip)
                .limit(limit)
                .toList();
    }

    public int size() {
        return properties.size();
    }

    public static LocalSettingLoader load(Class<?> appClz)
            throws IOException {
        return new LocalSettingLoader(openConfigInput(appClz));
    }

    private static InputStream openConfigInput(Class<?> appClz) throws IOException {
        File confFile = tryFile();
        if (!confFile.exists()) {
            return appClz.getResourceAsStream("/lamp.conf");
        }
        return new FileInputStream(confFile);
    }

    private static File tryFile() {
        File confFile = new File("conf/lamp.conf");
        if (confFile.exists()) {
            return confFile;
        }
        return new File("lamp.conf");
    }
}
