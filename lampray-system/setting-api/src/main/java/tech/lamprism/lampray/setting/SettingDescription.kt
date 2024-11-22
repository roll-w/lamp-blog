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

package tech.lamprism.lampray.setting

/**
 * Setting description. It can be a string or a resource.
 *
 * Has two implementations: [Text] and [Resource].
 *
 * @author RollW
 */
sealed interface SettingDescription {
    val value: String

    data class Text(override val value: String) : SettingDescription

    data class Resource(override val value: String) : SettingDescription

    companion object {
        @JvmField
        val EMPTY: SettingDescription = text("")

        /**
         * Create a string setting description.
         *
         * @param value The description.
         */
        @JvmStatic
        fun text(value: String): SettingDescription = Text(value)

        /**
         * Create a resource setting description.
         *
         * @param value The resource key.
         */
        @JvmStatic
        fun resource(value: String): SettingDescription = Resource(value)
    }
}

