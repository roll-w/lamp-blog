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
@JvmDefaultWithoutCompatibility
interface ConfigWriter {
    /**
     * Store the raw value of the setting.
     *
     * Not recommended to use this method directly, as it
     * will not check the validity of the value. Use the
     * [SettingSpecification] version instead.
     */
    operator fun set(key: String, value: String?)

    /**
     * Store the value of the setting.
     */
    operator fun <T, V> set(spec: SettingSpecification<T, V>, value: T?)

    fun supports(spec: SettingSpecification<*, *>): Boolean {
        return supports(spec.key.name)
    }

    fun supports(key: String): Boolean
}