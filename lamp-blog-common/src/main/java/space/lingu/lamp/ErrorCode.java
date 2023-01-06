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

package space.lingu.lamp;

import space.lingu.NonNull;

import java.util.Locale;

/**
 * <h2>Error Code</h2>
 *
 * <h3>Error Code Naming Format</h3>
 * <ul>
 *     <li>Success: {@code SUCCESS}</li>
 *     <li>Error: {@code ERROR_{Specify error name}}, like {@code ERROR_FILE_NOT_EXIST}.</li>
 * </ul>
 *
 * Class naming format:
 *  {@code {Group}ErrorCode}, like {@code FileErrorCode}.
 * <p>
 * This affects the generation of the i18n key.
 * <p>
 * The final key format is: {@code error.{Group}.{Specify error name}},
 * like {@code error.file.file_not_exist}.
 *
 * @author RollW
 */
public interface ErrorCode extends ErrorCodeFinder, ErrorCodeMessageProvider {
    String SUCCESS_CODE = "00000";

    @NonNull
    String getCode();

    @NonNull
    String getName();

    boolean getState();

    int getStatus();

    String toString();

    @Override
    default String apply(ErrorCode errorCode, Locale locale, Object... args) {
        return toString();
    }
}
