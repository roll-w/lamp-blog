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

package space.lingu.lamp.web.system.resource;

import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.data.page.Page;
import space.lingu.lamp.data.page.Pageable;
import space.lingu.lamp.web.system.MessageResource;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author RollW
 */
public interface MessageResourceLoader {
    @Nullable
    MessageResource getMessageResource(@NonNull String key,
                                       @NonNull Locale locale);


    @Nullable
    MessageResource getMessageResource(@NonNull String key,
                                       @NonNull Locale locale,
                                       String defaultValue);

    @Nullable
    MessageResource getMessageResource(@NonNull String key,
                                       @NonNull Locale locale,
                                       @NonNull Supplier<String> defaultValueProvider);

    Page<MessageResource> getMessageResources(Pageable pageable);

    List<MessageResource> getMessageResources(String key);
}
