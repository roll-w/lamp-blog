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
import space.lingu.lamp.web.common.WebCommonErrorCode;
import space.lingu.lamp.web.i18n.ErrorCodeKeyHelper;
import space.lingu.lamp.web.service.auth.AuthErrorCode;
import space.lingu.lamp.web.service.user.UserErrorCode;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
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
                UserErrorCode.class, WebCommonErrorCode.class
        );
        System.out.println("Start generate keys for ErrorCodes: " + errorCodeClasses);
        System.out.println();

        KeyGenerator generator = new KeyGenerator(errorCodeClasses);
        PrintWriter writer = new PrintWriter(System.out);
        generator.write(writer);
        System.out.println("\nEnd.");

        writer.close();
    }

    private final List<Class<? extends ErrorCode>> errorCodeClasses;
    private final Set<Key> keys = new HashSet<>();

    public KeyGenerator(List<Class<? extends ErrorCode>> errorCodeClasses) {
        this.errorCodeClasses = errorCodeClasses;
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

    public void write(PrintWriter writer) {
        errorCodeClasses.forEach(this::putSet);
        List<String> keys = convertToKeyNames();
        writer.println("success=");
        keys.forEach(key -> writer.println(key + "="));
        writer.flush();
    }

    private record Key(String className,
                       String codeName) {
    }

}
