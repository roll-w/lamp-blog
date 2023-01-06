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

package space.lingu.lamp.web.i18n;

import com.google.common.base.CaseFormat;
import space.lingu.lamp.ErrorCode;

/**
 * @author RollW
 */
public class ErrorCodeKeyHelper {
    private static final String SUCCESS_KEY = "success";

    public static String getI18nKey(ErrorCode errorCode) {
        if (errorCode == null) {
            return null;
        }
        String name = errorCode.getName();
        String className = errorCode.getClass().getSimpleName();
        return convertToKeyName(className, name);
    }

    public static String convertToKeyName(String className, String errorCodeName) {
        if (errorCodeName.equalsIgnoreCase("SUCCESS")) {
            return SUCCESS_KEY;
        }
        String group = toGroupName(className);
        String errorCodeKey = toCodeKey(errorCodeName);
        return "error." + group + "." + errorCodeKey;
    }

    public static String toCodeKey(String errorCodeName) {
        if (errorCodeName.startsWith("ERROR_")) {
            return errorCodeName.substring(6).toLowerCase();
        }
        return errorCodeName.toLowerCase();
    }

    public static String toGroupName(String className) {
        String underscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className);
        if (underscore.equals("error_code")) {
            throw new IllegalStateException("Cannot get group from class name: " + className);
        }
        if (underscore.endsWith("error_code")) {
            return underscore.substring(0, underscore.length() - 11);
        }
        return underscore;
    }

    private ErrorCodeKeyHelper() {
    }
}
