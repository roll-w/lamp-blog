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
package space.lingu.lamp.web.domain.content.service

import org.springframework.stereotype.Service
import space.lingu.lamp.web.domain.content.ContentMetadata
import space.lingu.lamp.web.domain.content.ContentTrait
import space.lingu.lamp.web.domain.content.common.ContentErrorCode
import space.lingu.lamp.web.domain.content.common.ContentException
import space.lingu.lamp.web.domain.content.repository.ContentMetadataRepository

/**
 * @author RollW
 */
@Service
class ContentMetadataServiceImpl(
    private val contentMetadataRepository: ContentMetadataRepository
) : ContentMetadataService {

    override fun getMetadata(contentTrait: ContentTrait): ContentMetadata {
        return contentMetadataRepository.getById(
            contentTrait.contentId,
            contentTrait.contentType
        ) ?: throw ContentException(
            ContentErrorCode.ERROR_CONTENT_NOT_FOUND,
            "Content metadata not found"
        )
    }

    fun createMetadata(metadata: ContentMetadata) {
        contentMetadataRepository.insert(metadata)
    }

    override fun updateMetadata(metadata: ContentMetadata) {
        contentMetadataRepository.update(metadata)
    }

    fun deleteMetadata(contentTrait: ContentTrait) {
    }
}
