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
package space.lingu.lamp.content.article.service

import org.springframework.stereotype.Service
import space.lingu.NonNull
import space.lingu.lamp.content.ContentDetails
import space.lingu.lamp.content.ContentOperator
import space.lingu.lamp.content.ContentProvider
import space.lingu.lamp.content.ContentTrait
import space.lingu.lamp.content.ContentType
import space.lingu.lamp.content.article.Article
import space.lingu.lamp.content.article.persistence.ArticleDo.Companion.toDo
import space.lingu.lamp.content.article.persistence.ArticleRepository
import space.lingu.lamp.content.common.ContentErrorCode
import space.lingu.lamp.content.common.ContentException
import space.lingu.lamp.content.service.ContentMetadataService

/**
 * @author RollW
 */
@Service
class ArticleProviderService(
    private val articleRepository: ArticleRepository,
    override val contentMetadataService: ContentMetadataService
) : ContentProvider,
    ArticleOperatorDelegate {
    override fun supports(@NonNull contentType: ContentType): Boolean {
        return contentType == ContentType.ARTICLE
    }

    override fun getContentDetails(@NonNull contentTrait: ContentTrait): ContentDetails {
        return articleRepository.findById(contentTrait.contentId).orElse(null)
            ?: throw ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND)
    }

    override fun getContentOperator(@NonNull contentTrait: ContentTrait, checkDelete: Boolean): ContentOperator {
        val article = articleRepository.findById(contentTrait.contentId).orElse(null)
            ?: throw ContentException(ContentErrorCode.ERROR_CONTENT_NOT_FOUND)

        return ArticleOperatorImpl(article.lock(), this, checkDelete)
    }

    override fun updateArticle(article: Article) {
        articleRepository.save(article.toDo())
    }
}
