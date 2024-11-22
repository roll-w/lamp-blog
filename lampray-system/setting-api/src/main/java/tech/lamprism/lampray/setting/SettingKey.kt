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
 * The key of a setting.
 *
 * @see SettingType
 * @author RollW
 */
data class SettingKey<T, V>(
    val name: String,
    val type: SettingType<T, V>
) {
    companion object {
        @JvmStatic
        fun <T, V> of(name: String, type: SettingType<T, V>): SettingKey<T, V> {
            return SettingKey(name, type)
        }

        @JvmStatic
        fun ofString(name: String): SettingKey<String, String> {
            return of(name, SettingType.STRING)
        }

        @JvmStatic
        fun ofStringSet(name: String): SettingKey<Set<String>, String> {
            return of(name, SettingType.STRING_SET)
        }

        @JvmStatic
        fun ofInt(name: String): SettingKey<Int, Int> {
            return of(name, SettingType.INT)
        }

        @JvmStatic
        fun ofLong(name: String): SettingKey<Long, Long> {
            return of(name, SettingType.LONG)
        }

        @JvmStatic
        fun ofFloat(name: String): SettingKey<Float, Float> {
            return of(name, SettingType.FLOAT)
        }

        @JvmStatic
        fun ofDouble(name: String): SettingKey<Double, Double> {
            return of(name, SettingType.DOUBLE)
        }

        @JvmStatic
        fun ofBoolean(name: String): SettingKey<Boolean, Boolean> {
            return of(name, SettingType.BOOLEAN)
        }
    }
}
