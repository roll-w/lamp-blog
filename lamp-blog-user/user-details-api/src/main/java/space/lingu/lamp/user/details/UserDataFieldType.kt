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
package space.lingu.lamp.user.details

/**
 * @author RollW
 */
class UserDataFieldType<T> private constructor() {
    companion object {
        @JvmField
        val AVATAR = UserDataFieldType<String>()

        @JvmField
        val NICKNAME = UserDataFieldType<String>()

        @JvmField
        val INTRO = UserDataFieldType<String>()

        @JvmField
        val GENDER = UserDataFieldType<Gender>()

        @JvmField
        val BIRTHDAY = UserDataFieldType<Birthday>()

        @JvmField
        val LOCATION = UserDataFieldType<String>()

        @JvmField
        val WEBSITE = UserDataFieldType<String>()
    }

    override fun toString(): String = when (this) {
        AVATAR -> "AVATAR"
        NICKNAME -> "NICKNAME"
        INTRO -> "INTRO"
        GENDER -> "GENDER"
        BIRTHDAY -> "BIRTHDAY"
        LOCATION -> "LOCATION"
        WEBSITE -> "WEBSITE"
        else -> "UNKNOWN"
    }
}
