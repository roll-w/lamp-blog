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

package tech.lamprism.lampray.web.common.keys

import tech.lamprism.lampray.setting.AttributedSettingSpecification
import tech.lamprism.lampray.setting.SettingKey
import tech.lamprism.lampray.setting.SettingSource
import tech.lamprism.lampray.setting.SettingSpecificationBuilder
import tech.lamprism.lampray.setting.SettingSpecificationSupplier

/**
 * @author RollW
 */
object ServerConfigKeys : SettingSpecificationSupplier {

    @JvmField
    val PORT =
        SettingSpecificationBuilder(SettingKey.ofInt("server.port"))
            .setTextDescription("Server port")
            .setDefaultValue(5100)
            .setSupportedSources(SettingSource.LOCAL_ONLY)
            .setRequired(true)
            .build()

    @JvmField
    val HOST =
        SettingSpecificationBuilder(SettingKey.ofString("server.host"))
            .setResourceDescription("config.server.host")
            .setDefaultValue(null)
            .setSupportedSources(SettingSource.LOCAL_ONLY)
            .setRequired(false)
            .build()

    private val keys = listOf(
        PORT
    )

    override val specifications: List<AttributedSettingSpecification<*, *>>
        get() = keys
}