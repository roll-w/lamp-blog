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

package space.lingu.lamp.web.domain.content.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.content.Content;
import space.lingu.lamp.web.domain.content.ContentAccessAuthType;
import space.lingu.lamp.web.domain.content.ContentMetadata;
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository;

/**
 * @author RollW
 */
@Component
public class ContentPublishEventListener implements ApplicationListener<ContentPublishEvent<?>> {
    private final ContentMetadataRepository contentMetadataRepository;

    public ContentPublishEventListener(ContentMetadataRepository contentMetadataRepository) {
        this.contentMetadataRepository = contentMetadataRepository;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull ContentPublishEvent<?> event) {
        if (!event.isFirstCreated()) {
            return;
        }
        Content content = event.getContent();
        ContentMetadata contentMetadata = ContentMetadata.builder()
                .setContentId(content.getContentId())
                .setContentType(content.getContentType())
                .setUserId(content.getUserId())
                .setContentStatus(event.getCurrentStatus())
                .setContentAccessAuthType(ContentAccessAuthType.PUBLIC)
                .build();
        contentMetadataRepository.insert(contentMetadata);
    }
}