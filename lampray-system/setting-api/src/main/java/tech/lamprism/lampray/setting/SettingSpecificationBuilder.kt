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

import tech.lamprism.lampray.setting.AttributedSettingSpec.Companion.withAttributes

/**
 * @author RollW
 */
class SettingSpecificationBuilder<T, V> {
    constructor()

    constructor(key: SettingKey<T, V>) {
        this.key = key
    }

    var key: SettingKey<T, V>? = null
        private set

    var allowAnyValue: Boolean = false
        private set

    var description: SettingDescription = SettingDescription.EMPTY
        private set

    var supportedSources: List<SettingSource> = SettingSource.LOCAL_ONLY
        private set

    var isRequired: Boolean = false
        private set

    var defaults: List<Int> = emptyList()
        private set

    var valueEntries: List<V?> = emptyList()
        private set

    fun setKey(key: SettingKey<T, V>): SettingSpecificationBuilder<T, V> {
        this.key = key
        return this
    }

    fun setDescription(description: SettingDescription): SettingSpecificationBuilder<T, V> {
        this.description = description
        return this
    }

    fun setTextDescription(description: String): SettingSpecificationBuilder<T, V> {
        this.description = SettingDescription.text(description)
        return this
    }

    fun setResourceDescription(key: String): SettingSpecificationBuilder<T, V> {
        this.description = SettingDescription.resource(key)
        return this
    }

    fun setAllowAnyValue(allowAnyValue: Boolean): SettingSpecificationBuilder<T, V> {
        this.allowAnyValue = allowAnyValue
        return this
    }

    fun setSupportedSources(supportedSources: List<SettingSource>): SettingSpecificationBuilder<T, V> {
        this.supportedSources = supportedSources
        return this
    }

    fun setRequired(isRequired: Boolean): SettingSpecificationBuilder<T, V> {
        this.isRequired = isRequired
        return this
    }

    fun setDefaults(defaults: List<Int>): SettingSpecificationBuilder<T, V> {
        this.defaults = defaults
        return this
    }

    fun setDefault(default: Int): SettingSpecificationBuilder<T, V> {
        this.defaults = listOf(default)
        return this
    }

    fun setDefaultValue(default: V?): SettingSpecificationBuilder<T, V> {
        this.defaults = listOf(0)
        this.valueEntries = listOf(default)
        return this
    }

    fun setValueEntries(valueEntries: List<V?>): SettingSpecificationBuilder<T, V> {
        this.valueEntries = valueEntries
        return this
    }

    fun buildSimple(): SettingSpecification<T, V> {
        return SimpleSettingSpec(
            key = key!!,
            allowAnyValue = allowAnyValue,
            defaults = defaults,
            valueEntries = valueEntries,
            isRequired = isRequired
        )
    }

    fun build(): AttributedSettingSpecification<T, V> {
        return buildSimple().withAttributes(
            description = description,
            supportedSources = supportedSources
        )
    }
}