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
package space.lingu.lamp.web.controller.comment.model

import space.lingu.lamp.content.ContentDetails
import space.lingu.lamp.content.ContentType
import space.lingu.lamp.content.comment.Comment
import space.lingu.lamp.web.controller.content.vo.ContentVo
import java.time.LocalDateTime

/**
 * @author RollW
 */
data class CommentVo(
    val id: Long,
    val userId: Long,
    val parent: Long,
    val content: String,
    val contentId: Long,
    val contentType: ContentType,
    val createTime: LocalDateTime,
    val updateTime: LocalDateTime
) : ContentVo {

    override fun id(): Long = id

    override fun createTime(): LocalDateTime = createTime

    override fun updateTime(): LocalDateTime = updateTime

    companion object {
        @JvmStatic
        fun of(contentDetails: ContentDetails?): CommentVo? {
            if (contentDetails == null) {
                return null
            }
            if (contentDetails !is Comment) {
                return null
            }
            return CommentVo(
                contentDetails.id,
                contentDetails.userId,
                contentDetails.parentId,
                contentDetails.content,
                contentDetails.commentOnId,
                contentDetails.commentOnType,
                contentDetails.createTime,
                contentDetails.updateTime
            )
        }
    }

}
