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

package space.lingu.lamp.content.article.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Lob
import jakarta.persistence.Table
import jakarta.persistence.Temporal
import jakarta.persistence.TemporalType
import space.lingu.lamp.DataEntity
import space.lingu.lamp.content.ContentDetails
import space.lingu.lamp.content.ContentDetailsMetadata
import space.lingu.lamp.content.ContentType
import space.lingu.lamp.content.article.Article
import space.lingu.lamp.content.article.ArticleDetailsMetadata
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * @author RollW
 */
@Entity
@Table(name = "article")
class ArticleDo(
    @Column(name = "id", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    private var userId: Long = 0,

    @Column(name = "title", nullable = false, length = 255)
    private var title: String = "",

    @Column(name = "cover", nullable = false, length = 255)
    var cover: String = "",

    @Lob
    @Column(name = "content", nullable = false, length = 20000000)
    private var content: String = "",

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", nullable = false)
    private var createTime: LocalDateTime = LocalDateTime.now(),

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = false)
    private var updateTime: LocalDateTime = LocalDateTime.now()
) : DataEntity<Long>, ContentDetails {
    override fun getId(): Long? = id

    override fun getResourceId(): Long = id!!

    @Deprecated("Deprecated in Java")
    override fun getCreateTime(): Long =
        createTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @Deprecated("Deprecated in Java")
    override fun getUpdateTime(): Long =
        updateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

    override fun getContentId(): Long = id!!

    override fun getContentType(): ContentType = ContentType.ARTICLE

    override fun getUserId(): Long = userId

    override fun getTitle(): String? = title

    override fun getContent(): String? = content

    override fun getMetadata(): ContentDetailsMetadata =
        ArticleDetailsMetadata(cover)

    fun lock(): Article = Article(
        id!!, userId, title, cover, content, createTime, updateTime
    )

    fun toBuilder(): Builder = Builder(this)

    override fun toString(): String {
        return "ArticleDo(" +
                "id=$id, " +
                "userId=$userId, " +
                "title='$title', " +
                "cover='$cover', " +
                "content='$content', " +
                "createTime=$createTime, " +
                "updateTime=$updateTime" +
                ")"
    }

    class Builder {
        private var id: Long? = null
        private var userId: Long = 0
        private var title: String? = null
        private var cover: String? = null
        private var content: String? = null
        private var createTime: LocalDateTime? = null
        private var updateTime: LocalDateTime? = null

        constructor()

        constructor(other: ArticleDo) {
            this.id = other.id
            this.userId = other.userId
            this.title = other.title
            this.cover = other.cover
            this.content = other.content
            this.createTime = other.createDateTime
            this.updateTime = other.updateDateTime
        }

        fun setId(id: Long?): Builder {
            this.id = id
            return this
        }

        fun setUserId(userId: Long): Builder {
            this.userId = userId
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setCover(cover: String): Builder {
            this.cover = cover
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

        fun build(): ArticleDo {
            return ArticleDo(
                id, userId, title!!, cover!!,
                content!!, createTime!!, updateTime!!
            )
        }
    }

    companion object {
        @JvmStatic
        fun Article.toDo(): ArticleDo {
            return ArticleDo(
                id, userId, title, cover, content,
                createDateTime, updateDateTime
            )
        }

        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }
}