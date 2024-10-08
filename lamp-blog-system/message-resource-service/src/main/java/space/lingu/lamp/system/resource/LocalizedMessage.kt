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
package space.lingu.lamp.system.resource

import space.lingu.NonNull
import space.lingu.lamp.DataEntity
import tech.rollw.common.web.system.SystemResourceKind
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

/**
 * Localized message resource, overrides the default message resource
 * in the system.
 *
 * Could be used to override the default i18n messages in the system.
 *
 * @author RollW
 */
data class LocalizedMessage(
    private val id: Long?,
    override val key: String,
    override val value: String,
    override val locale: Locale,
    private val updateTime: LocalDateTime
) : LocalizedMessageResource, DataEntity<Long> {
    override fun getId(): Long? {
        return id
    }

    override fun getCreateTime(): Long {
        return 0
    }

    override fun getUpdateTime(): Long {
        return updateTime.toEpochSecond(ZoneOffset.UTC)
    }

    @NonNull
    override fun getSystemResourceKind(): SystemResourceKind {
        return LocalizedMessageResourceKind
    }

    fun toBuilder(): Builder {
        return Builder(this)
    }

    class Builder {
        private var id: Long? = null
        private var key: String? = null
        private var value: String? = null
        private var locale: Locale? = null
        private var updateTime: LocalDateTime? = null

        constructor()

        constructor(localizedMessage: LocalizedMessage) {
            this.id = localizedMessage.id
            this.key = localizedMessage.key
            this.value = localizedMessage.value
            this.locale = localizedMessage.locale
            this.updateTime = localizedMessage.updateTime
        }

        fun setId(id: Long?): Builder {
            this.id = id
            return this
        }

        fun setKey(key: String): Builder {
            this.key = key
            return this
        }

        fun setValue(value: String): Builder {
            this.value = value
            return this
        }

        fun setLocale(locale: Locale): Builder {
            this.locale = locale
            return this
        }

        fun setUpdateTime(updateTime: LocalDateTime): Builder {
            this.updateTime = updateTime
            return this
        }

        fun build(): LocalizedMessage {
            return LocalizedMessage(id, key!!, value!!, locale!!, updateTime!!)
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }

        fun of(
            key: String, value: String,
            locale: Locale
        ): LocalizedMessage {
            return builder()
                .setKey(key)
                .setValue(value)
                .setLocale(locale)
                .build()
        }
    }
}
