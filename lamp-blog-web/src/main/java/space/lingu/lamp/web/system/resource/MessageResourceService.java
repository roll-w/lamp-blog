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

import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.web.system.MessageResource;
import space.lingu.lamp.web.system.repository.MessageResourceRepository;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;

import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

/**
 * @author RollW
 */
@Service
public class MessageResourceService implements MessageResourceProvider, MessageResourceLoader {
    private final MessageResourceRepository messageResourceRepository;

    public MessageResourceService(MessageResourceRepository messageResourceRepository) {
        this.messageResourceRepository = messageResourceRepository;
    }

    @Nullable
    public MessageResource getMessageResource(
            @NonNull String key,
            @NonNull Locale locale) {
        return messageResourceRepository.getByKeyAndLocale(key, locale);
    }

    @Nullable
    @Override
    public MessageResource getMessageResource(@NonNull String key,
                                              @NonNull Locale locale,
                                              String defaultValue) {
        MessageResource messageResource = getMessageResource(key, locale);
        if (messageResource == null) {
            return MessageResource.of(key, defaultValue, locale);
        }
        return messageResource;
    }

    @Nullable
    @Override
    public MessageResource getMessageResource(@NonNull String key,
                                              @NonNull Locale locale,
                                              @NonNull Supplier<String> defaultValueProvider) {
        MessageResource messageResource = getMessageResource(key, locale);
        if (messageResource == null) {
            return MessageResource.of(key, defaultValueProvider.get(), locale);
        }
        return messageResource;
    }

    @Override
    public Page<MessageResource> getMessageResources(Pageable pageable) {
        List<MessageResource> messageResources =
                messageResourceRepository.get(pageable.toOffset());
        return Page.of(pageable, 1, messageResources);
    }

    @Override
    public List<MessageResource> getMessageResources(String key) {
        return messageResourceRepository.getByKey(key);
    }

    @Override
    public void setMessageResource(@NonNull String key,
                                   @NonNull String value,
                                   @NonNull Locale locale) {

    }

    @Override
    public void setMessageResource(@NonNull MessageResource messageResource) {

    }

    @Override
    public void deleteMessageResource(@NonNull String key) {

    }

    @Override
    public void deleteMessageResource(@NonNull String key, @NonNull Locale locale) {

    }
}
