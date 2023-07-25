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

package space.lingu.lamp.web.system;

import tech.rollw.common.web.ErrorCode;

/**
 * @author RollW
 */
public record ErrorRecordVo(
        ErrorCode errorCode,
        String message,
        String exceptionName,
        String stacktrace,
        long timestamp
) {
    public static ErrorRecordVo from(ErrorRecord errorRecord) {
        if (errorRecord == null) {
            return null;
        }
        return new ErrorRecordVo(
                errorRecord.errorCode(),
                errorRecord.throwable().getMessage(),
                errorRecord.throwable().getClass().getName(),
                formatStacktrace(errorRecord.throwable().getStackTrace()),
                errorRecord.timestamp()
        );
    }

    private static String formatStacktrace(StackTraceElement[] elements) {
        StringBuilder builder = new StringBuilder();
        // max 12 elements
        int remain = elements.length - 12;
        for (int i = 0; i < elements.length && i < 12; i++) {
            builder.append(elements[i]).append("\n");
        }
        if (remain > 0) {
            builder.append("\n\n  ").append(remain).append(" more...");
        }

        return builder.toString();
    }
}
