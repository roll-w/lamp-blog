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

package tech.lamprism.lampray.content.comment.persistence

import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Repository
import tech.lamprism.lampray.common.data.CommonRepository
import tech.lamprism.lampray.content.ContentType

/**
 * @author RollW
 */
@Repository
class CommentRepository(
    private val commentDao: CommentDao
) : CommonRepository<CommentDo, Long>(commentDao) {
    fun findByContent(
        contentId: Long,
        contentType: ContentType
    ): List<CommentDo> {
        return findAll(createContentSpecification(contentId, contentType))
    }

    private fun createContentSpecification(
        contentId: Long,
        contentType: ContentType
    ): Specification<CommentDo> = Specification { root, query, criteriaBuilder ->
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CommentDo_.commentOnId), contentId),
            criteriaBuilder.equal(root.get(CommentDo_.commentOnType), contentType)
        )
    }
}