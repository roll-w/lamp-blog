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
package space.lingu.lamp.web.domain.noticeboard

import space.lingu.NonNull
import space.lingu.lamp.LongDataItem
import space.lingu.lamp.LongEntityBuilder
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind
import space.lingu.light.DataColumn
import space.lingu.light.DataTable
import space.lingu.light.PrimaryKey
import space.lingu.light.SQLDataType
import tech.rollw.common.web.system.SystemResourceKind

/**
 * Notice board, system-wide announcement.
 *
 * @author RollW
 */
@DataTable(name = "notice_board")
data class NoticeBoard(
    @PrimaryKey(autoGenerate = true)
    @DataColumn(name = "id")
    private val id: Long?,

    @DataColumn(name = "title") val title: String,

    @DataColumn(name = "content", dataType = SQLDataType.LONGTEXT)
    val content: String?,
    /**
     * Creator. Needs to be ADMIN or EDITOR.
     */
    @DataColumn(name = "user_id")
    val userId: Long,
    @DataColumn(name = "create_time")
    private val createTime: Long,
    @DataColumn(name = "update_time")
    private val updateTime: Long
) : LongDataItem<NoticeBoard> {
    override fun getId(): Long? {
        return id
    }

    override fun getCreateTime(): Long {
        return createTime
    }

    override fun getUpdateTime(): Long {
        return updateTime
    }

    @NonNull
    override fun getSystemResourceKind(): SystemResourceKind {
        return LampSystemResourceKind.SYSTEM_NOTICE_BOARD
    }

    override fun toBuilder(): Builder {
        return Builder(this)
    }

    class Builder : LongEntityBuilder<NoticeBoard> {
        private var id: Long? = null
        private var title: String = ""
        private var content: String? = null
        private var userId: Long = 0
        private var createTime: Long = 0
        private var updateTime: Long = 0

        constructor()

        internal constructor(noticeboard: NoticeBoard) {
            this.id = noticeboard.id
            this.title = noticeboard.title
            this.content = noticeboard.content
            this.userId = noticeboard.userId
            this.createTime = noticeboard.createTime
            this.updateTime = noticeboard.updateTime
        }

        override fun setId(id: Long?): Builder {
            this.id = id
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setContent(content: String?): Builder {
            this.content = content
            return this
        }

        fun setUserId(userId: Long): Builder {
            this.userId = userId
            return this
        }

        fun setCreateTime(createTime: Long): Builder {
            this.createTime = createTime
            return this
        }

        fun setUpdateTime(updateTime: Long): Builder {
            this.updateTime = updateTime
            return this
        }

        override fun build(): NoticeBoard {
            return NoticeBoard(id, title, content, userId, createTime, updateTime)
        }
    }

    companion object {
        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }
}
