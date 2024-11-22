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
 * Setting specification.
 *
 * @author RollW
 */
@JvmDefaultWithoutCompatibility
interface SettingSpecification<T, V> {
    val key: SettingKey<T, V>

    /**
     * The default value indexes in [valueEntries].
     *
     * Only [SettingType.STRING_SET] type can have multiple defaults.
     */
    val defaults: List<Int>

    /**
     * Leave it empty if the setting has no predefined values.
     *
     * The order of the values should be the same as the order of the default values.
     *
     * The values in the entries should be unique and could be null.
     */
    val valueEntries: List<V?>

    val defaultValue: T?

    /**
     * Whether the setting is required. If the setting is required,
     * the value of the setting must be set and cannot be null.
     */
    val isRequired : Boolean

    operator fun get(index: Int): V?

    fun hasValue(value: V?): Boolean

    fun allowAnyValue(): Boolean

    companion object {
        @JvmStatic
        val SettingSpecification<*, *>.keyName: String
            get() = key.name
    }
}
