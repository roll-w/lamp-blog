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

package space.lingu.lamp.system.resource;

import space.lingu.NonNull;
import space.lingu.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author RollW
 */
public interface LocalizedMessageResourceLoader {
    void setFallbackLocale(@NonNull Locale locale);

    @NonNull
    Locale getFallbackLocale();

    @Nullable
    LocalizedMessageResource getMessageResource(@NonNull String key,
                                                @NonNull Locale locale);

    @Nullable
    LocalizedMessageResource getMessageResource(@NonNull String key,
                                                @NonNull Locale locale,
                                                String defaultValue);

    @Nullable
    LocalizedMessageResource getMessageResource(@NonNull String key,
                                                @NonNull Locale locale,
                                                @NonNull Supplier<String> defaultValueProvider);

    List<? extends LocalizedMessageResource> getMessageResources();

    List<? extends LocalizedMessageResource> getMessageResources(String key);
}
