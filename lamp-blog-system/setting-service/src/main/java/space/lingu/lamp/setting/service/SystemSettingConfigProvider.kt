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

package space.lingu.lamp.setting.service

import org.springframework.stereotype.Service
import space.lingu.lamp.setting.ConfigProvider
import space.lingu.lamp.setting.RawSettingValue
import space.lingu.lamp.setting.SettingSpecification
import space.lingu.lamp.setting.SettingSpecification.Companion.keyName
import space.lingu.lamp.setting.SettingSpecificationHelper
import space.lingu.lamp.setting.data.SystemSettingDo
import space.lingu.lamp.setting.data.SystemSettingRepository

/**
 * @author RollW
 */
@Service
class SystemSettingConfigProvider(
    private val systemSettingRepository: SystemSettingRepository
) : ConfigProvider {
    override fun get(key: String): String? {
        return systemSettingRepository.findByKey(key)?.value
    }

    override fun get(key: String, defaultValue: String?): String? =
        get(key) ?: defaultValue

    @JvmName("getOrDefault")
    @Suppress("INAPPLICABLE_JVM_NAME")
    override fun get(key: String, defaultValue: String): String =
        get(key) ?: defaultValue

    override fun <T, V> get(specification: SettingSpecification<T, V>): T? {
        val setting = systemSettingRepository.findByKey(specification.keyName)
            ?: return null
        return with(SettingSpecificationHelper) {
            setting.value.deserialize(specification)
        }
    }

    override fun <T, V> get(
        specification: SettingSpecification<T, V>,
        defaultValue: T
    ): T = get(specification) ?: defaultValue

    override fun list(): List<RawSettingValue> {
        return systemSettingRepository.findAll().map {
            RawSettingValue(
                it.key,
                it.value,
                RawSettingValue.Source.DATABASE
            )
        }
    }

    override fun set(key: String, value: String?) {
        val setting = systemSettingRepository.findByKey(key)
        if (setting != null) {
            setting.value = value
            systemSettingRepository.save(setting)
            return
        }
        val newSetting = SystemSettingDo(
            key = key,
            value = value
        )
        systemSettingRepository.save(newSetting)
    }

    override fun <T, V> set(spec: SettingSpecification<T, V>, value: T?) {
        val setting = systemSettingRepository.findByKey(spec.keyName)
        val value = with(SettingSpecificationHelper) {
            value.serialize(spec)
        }
        if (setting != null) {
            setting.value = value
            systemSettingRepository.save(setting)
            return
        }
        val newSetting = SystemSettingDo(
            key = spec.key.name,
            value = value
        )
        systemSettingRepository.save(newSetting)
    }

    override fun supports(key: String): Boolean = true
}