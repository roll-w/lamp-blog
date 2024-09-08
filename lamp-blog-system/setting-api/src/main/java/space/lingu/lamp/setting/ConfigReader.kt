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

package space.lingu.lamp.setting

/**
 * @author RollW
 */
interface ConfigReader {
    operator fun get(key: String): String?

    operator fun get(key: String, defaultValue: String?): String?

    @JvmName("getOrDefault")
    @Suppress("INAPPLICABLE_JVM_NAME")
    operator fun get(key: String, defaultValue: String): String

    operator fun <T, V> get(specification: SettingSpecification<T, V>): T?

    operator fun <T, V> get(specification: SettingSpecification<T, V>, defaultValue: T): T

    fun list(): List<RawSettingValue>
}