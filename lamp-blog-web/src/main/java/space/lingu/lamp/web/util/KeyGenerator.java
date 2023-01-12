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

package space.lingu.lamp.web.util;

import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCode;
import space.lingu.lamp.IoErrorCode;
import space.lingu.lamp.web.common.DataErrorCode;
import space.lingu.lamp.web.common.WebCommonErrorCode;
import space.lingu.lamp.web.i18n.ErrorCodeKeyHelper;
import space.lingu.lamp.web.service.auth.AuthErrorCode;
import space.lingu.lamp.web.service.user.UserErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Helper class for reading error codes and
 * converts to properties keys.
 *
 * @author RollW
 */
public class KeyGenerator {
    public static void main(String[] args) {
        List<Class<? extends ErrorCode>> errorCodeClasses = List.of(
                CommonErrorCode.class, AuthErrorCode.class, IoErrorCode.class,
                UserErrorCode.class, WebCommonErrorCode.class, DataErrorCode.class
        );
        String[] properties = new String[]{
                "messages.properties",
                "messages_en_US.properties",
                "messages_zh_CN.properties"
        };

        System.out.println("Start generate keys for ErrorCodes: " + errorCodeClasses);
        System.out.println();

        KeyGenerator generator = new KeyGenerator(errorCodeClasses);
        PrintWriter writer = new PrintWriter(System.out);
        List<String> keyNames = generator.keyNames();

        for (String property : properties) {
            writer.println(property + "\n============");
            Map<String, KeyPair> keyPairs = readKeyPairs(property);
            for (String keyName : keyNames) {
                KeyPair keyPair = keyPairs.get(keyName);
                if (keyPair == null) {
                    writer.println(keyName + "=");
                } else {
                    writer.println(keyPair);
                }
            }
            writer.println();
            writer.flush();
        }

        System.out.println("\nEnd.");

        writer.close();
    }

    private static Map<String, KeyPair> readKeyPairs(String property) {
        Properties properties = load(property);
        return readProperties(properties);
    }

    private static Properties load(String path) {
        Properties properties = new Properties();
        try (InputStream in = KeyGenerator.class.getResourceAsStream("/" + path)) {
            if (in == null) {
                throw new IOException("Cannot find resource: " + path);
            }
            Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load properties: " + path, e);
        }
        return properties;
    }

    private static Map<String, KeyPair> readProperties(Properties properties) {
        Map<String, KeyPair> keyPairs = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            keyPairs.put(key, new KeyPair(key, value));
        }
        return keyPairs;
    }

    private final Set<Key> keys = new HashSet<>();

    public KeyGenerator(List<Class<? extends ErrorCode>> errorCodeClasses) {
        errorCodeClasses.forEach(this::putSet);
    }

    private void putSet(Class<? extends ErrorCode> clz) {
        if (clz.isEnum()) {
            putEnumConstants(clz);
            return;
        }
        throw new IllegalStateException("Not support types except enum. " + clz);
    }

    private void putEnumConstants(Class<? extends ErrorCode> clz) {
        ErrorCode[] codes = clz.getEnumConstants();
        for (ErrorCode code : codes) {
            Key key = new Key(clz.getSimpleName(), code.getName());
            keys.add(key);
        }
    }

    private List<String> convertToKeyNames() {
        List<String> processed = keys.stream()
                .map(key -> ErrorCodeKeyHelper.convertToKeyName(key.className, key.codeName))
                .toList();
        Set<String> distinct = new HashSet<>();
        processed.stream()
                .filter(s -> !s.equalsIgnoreCase("success"))
                .forEach(s -> {
                    if (distinct.contains(s)) {
                        throw new IllegalStateException("Duplicated key: " + s);
                    }
                    distinct.add(s);
                });
        return distinct.stream().sorted().toList();
    }

    public List<String> keyNames() {
        List<String> keys = new ArrayList<>(convertToKeyNames());
        keys.add(0, "success");
        return keys;
    }

    public void write(PrintWriter writer) {
        List<String> keys = convertToKeyNames();
        writer.println("success=");
        keys.forEach(key -> writer.println(key + "="));
        writer.flush();
    }

    private record Key(String className,
                       String codeName) {
    }

    private record KeyPair(String key,
                           String value) {
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }


}
