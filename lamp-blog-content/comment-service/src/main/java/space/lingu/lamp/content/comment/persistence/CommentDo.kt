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

package space.lingu.lamp.content.comment.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import space.lingu.lamp.DataEntity
import space.lingu.lamp.content.ContentAssociated
import space.lingu.lamp.content.ContentDetails
import space.lingu.lamp.content.ContentIdentity
import space.lingu.lamp.content.ContentType
import space.lingu.lamp.content.comment.Comment
import space.lingu.lamp.content.comment.CommentStatus
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author RollW
 */
@Entity
@Table(name = "comment")
class CommentDo(
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    private var userId: Long = 0,

    @Column(name = "parent_id", nullable = false)
    var parentId: Long = 0,

    @Lob
    @Column(name = "content", nullable = false, length = 2000)
    private var content: String = "",

    @Column(name = "create_time", nullable = false)
    private var createTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_time", nullable = false)
    private var updateTime: LocalDateTime = LocalDateTime.now(),

    @Column(name = "comment_on_type", nullable = false)
    @Enumerated(EnumType.STRING)
    var commentOnType: ContentType = ContentType.COMMENT,

    @Column(name = "comment_on_id", nullable = false)
    var commentOnId: Long = 0,

    @Column(name = "comment_status", nullable = false)
    @Enumerated(EnumType.STRING)
    var commentStatus: CommentStatus = CommentStatus.NONE
) : DataEntity<Long>, ContentDetails, ContentAssociated {
    override fun getId(): Long? = id

    override fun getResourceId(): Long = id!!

    @Deprecated("Deprecated in Java")
    override fun getCreateTime(): Long =
        createTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @Deprecated("Deprecated in Java")
    override fun getUpdateTime(): Long =
        updateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    override fun getContentId(): Long = id!!

    override fun getContentType(): ContentType =
        ContentType.COMMENT

    override fun getUserId(): Long = userId

    override fun getTitle(): String? = null

    override fun getContent(): String = content

    override fun getAssociatedContent(): ContentIdentity =
        ContentIdentity.of(commentOnId, commentOnType)

    fun lock(): Comment = Comment(
        id!!, userId, parentId, content, createTime,
        updateTime, commentOnType, commentOnId, commentStatus
    )

    class Builder {
        private var id: Long? = null
        private var userId: Long = 0
        private var parentId: Long = 0
        private var content: String? = null
        private var createTime: LocalDateTime? = null
        private var updateTime: LocalDateTime? = null
        private var commentOnType: ContentType? = null
        private var commentOnId: Long = 0
        private var commentStatus: CommentStatus? = null

        constructor()

        constructor(other: CommentDo) {
            this.id = other.id
            this.userId = other.userId
            this.parentId = other.parentId
            this.content = other.content
            this.createTime = other.createDateTime
            this.updateTime = other.updateDateTime
            this.commentOnType = other.commentOnType
            this.commentOnId = other.commentOnId
            this.commentStatus = other.commentStatus
        }

        fun setId(id: Long?): Builder {
            this.id = id
            return this
        }

        fun setUserId(userId: Long): Builder {
            this.userId = userId
            return this
        }

        fun setParentId(parentId: Long): Builder {
            this.parentId = parentId
            return this
        }

        fun setContent(content: String): Builder {
            this.content = content
            return this
        }

        fun setCreateTime(createTime: LocalDateTime): Builder {
            this.createTime = createTime
            return this
        }

        fun setUpdateTime(updateTime: LocalDateTime): Builder {
            this.updateTime = updateTime
            return this
        }

        fun setCommentOnType(commentOnType: ContentType): Builder {
            this.commentOnType = commentOnType
            return this
        }

        fun setCommentOnId(commentOnId: Long): Builder {
            this.commentOnId = commentOnId
            return this
        }

        fun setCommentStatus(commentStatus: CommentStatus): Builder {
            this.commentStatus = commentStatus
            return this
        }

        fun build(): CommentDo {
            return CommentDo(
                id,
                userId,
                parentId,
                content!!,
                createTime!!,
                updateTime!!,
                commentOnType!!,
                commentOnId,
                commentStatus!!
            )
        }
    }

    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()

        @JvmStatic
        fun Comment.toDo(): CommentDo {
            return CommentDo(
                id, userId, parentId, content,
                createDateTime, updateDateTime,
                commentOnType, commentOnId, commentStatus
            )
        }
    }
}