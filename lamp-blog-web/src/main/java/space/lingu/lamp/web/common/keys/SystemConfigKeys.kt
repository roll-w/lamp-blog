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
package space.lingu.lamp.web.common.keys

import space.lingu.lamp.setting.AttributedSettingSpecification
import space.lingu.lamp.setting.SettingKey
import space.lingu.lamp.setting.SettingSource
import space.lingu.lamp.setting.SettingSpecificationBuilder
import space.lingu.lamp.setting.SettingSpecificationSupplier

/**
 * @author RollW
 */
object SystemConfigKeys: SettingSpecificationSupplier {
    @JvmField
    val APP_NAME =
        SettingSpecificationBuilder(SettingKey.ofString("system.app.name"))
            .setDefaultValue("Lamp Blog")
            .setRequired(true)
            .setTextDescription("The name of the application.")
            .setSupportedSources(SettingSource.LOCAL_ONLY)
            .build()

    private val keys = listOf(APP_NAME)

    override val specifications: List<AttributedSettingSpecification<*, *>>
        get() = keys
}
