/*
 * Copyright (C) 2024 RollW
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

package space.lingu.lamp.setting

/**
 * Setting type.
 *
 * @param T the type that will be stored (in other words, the actual stored value type)
 * @param V if the type is a collection, the type of the elements in the collection
 * @author RollW
 */
class SettingType<T, V> private constructor() {
    companion object {
        @JvmField
        val STRING = SettingType<String, String>()
        @JvmField
        val INT = SettingType<Int, Int>()
        @JvmField
        val LONG = SettingType<Long, Long>()
        @JvmField
        val FLOAT = SettingType<Float, Float>()
        @JvmField
        val DOUBLE = SettingType<Double, Double>()
        @JvmField
        val BOOLEAN = SettingType<Boolean, Boolean>()
        @JvmField
        val STRING_SET = SettingType<Set<String>, String>()

        fun of(value: Any): SettingType<*, *> = when (value) {
            is String -> STRING
            is Int -> INT
            is Long -> LONG
            is Float -> FLOAT
            is Double -> DOUBLE
            is Boolean -> BOOLEAN
            is Set<*> -> STRING_SET
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java}")
        }
    }

    override fun toString(): String {
        return when (this) {
            STRING -> "STRING"
            INT -> "INT"
            LONG -> "LONG"
            FLOAT -> "FLOAT"
            DOUBLE -> "DOUBLE"
            BOOLEAN -> "BOOLEAN"
            STRING_SET -> "STRING_SET"
            else -> throw IllegalArgumentException("Unsupported type: $this")
        }
    }
}