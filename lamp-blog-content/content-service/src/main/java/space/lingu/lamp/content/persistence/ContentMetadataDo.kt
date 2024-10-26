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

package space.lingu.lamp.content.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import space.lingu.lamp.DataEntity
import space.lingu.lamp.TimeAttributed
import space.lingu.lamp.content.ContentAccessAuthType
import space.lingu.lamp.content.ContentMetadata
import space.lingu.lamp.content.ContentMetadataResourceKind
import space.lingu.lamp.content.ContentStatus
import space.lingu.lamp.content.ContentTrait
import space.lingu.lamp.content.ContentType
import tech.rollw.common.web.system.SystemResourceKind
import java.time.LocalDateTime

/**
 * @author RollW
 */
@Entity
@Table(
    name = "content_metadata", uniqueConstraints = [
        UniqueConstraint(columnNames = ["content_id", "type"])
    ]
)
class ContentMetadataDo(
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: Long = 0,

    @Column(name = "content_id", nullable = false)
    private var contentId: Long = 0,

    @Column(name = "type", nullable = false, length = 40)
    @Enumerated(EnumType.STRING)
    private var contentType: ContentType = ContentType.ARTICLE,

    @Column(name = "status", nullable = false, length = 40)
    @Enumerated(EnumType.STRING)
    var contentStatus: ContentStatus = ContentStatus.REVIEWING,

    @Column(name = "auth_type", nullable = false)
    var contentAccessAuthType: ContentAccessAuthType = ContentAccessAuthType.PUBLIC
) : DataEntity<Long>, ContentTrait {
    override fun getId(): Long? = id

    fun setId(id: Long?) {
        this.id = id
    }

    override fun getCreateTime(): LocalDateTime = TimeAttributed.NONE_TIME

    override fun getUpdateTime(): LocalDateTime = TimeAttributed.NONE_TIME

    override fun getSystemResourceKind(): SystemResourceKind =
        ContentMetadataResourceKind

    override fun getContentId(): Long = contentId

    fun setContentId(contentId: Long) {
        this.contentId = contentId
    }

    override fun getContentType(): ContentType = contentType

    fun setContentType(contentType: ContentType) {
        this.contentType = contentType
    }

    fun lock(): ContentMetadata = ContentMetadata(
        id, userId, contentId, contentType, contentStatus, contentAccessAuthType
    )

    fun toBuilder(): Builder {
        return Builder(this)
    }

    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()

        @JvmStatic
        fun ContentMetadata.toDo() =
            ContentMetadataDo(
                id, userId, contentId, contentType,
                contentStatus, contentAccessAuthType
            )

    }

    class Builder {
        private var id: Long? = null
        private var userId: Long = 0
        private var contentId: Long = 0
        private var contentType: ContentType? = null
        private var contentStatus: ContentStatus? = null
        private var contentAccessAuthType: ContentAccessAuthType? = null

        constructor()

        constructor(other: ContentMetadataDo) {
            this.id = other.getId()
            this.userId = other.userId
            this.contentId = other.getContentId()
            this.contentType = other.getContentType()
            this.contentStatus = other.contentStatus
            this.contentAccessAuthType = other.contentAccessAuthType
        }

        fun setId(id: Long?): Builder {
            this.id = id
            return this
        }

        fun setUserId(userId: Long): Builder {
            this.userId = userId
            return this
        }

        fun setContentId(contentId: Long): Builder {
            this.contentId = contentId
            return this
        }

        fun setContentType(contentType: ContentType): Builder {
            this.contentType = contentType
            return this
        }

        fun setContentStatus(contentStatus: ContentStatus): Builder {
            this.contentStatus = contentStatus
            return this
        }

        fun setContentAccessAuthType(contentAccessAuthType: ContentAccessAuthType): Builder {
            this.contentAccessAuthType = contentAccessAuthType
            return this
        }

        fun build(): ContentMetadataDo {
            return ContentMetadataDo(
                id, userId,
                contentId,
                contentType!!, contentStatus!!,
                contentAccessAuthType!!
            )
        }
    }
}