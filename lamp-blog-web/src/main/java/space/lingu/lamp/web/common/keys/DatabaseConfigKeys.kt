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
import space.lingu.lamp.setting.SettingDescription
import space.lingu.lamp.setting.SettingKey
import space.lingu.lamp.setting.SettingSource
import space.lingu.lamp.setting.SettingSpecificationBuilder
import space.lingu.lamp.setting.SettingSpecificationSupplier

/**
 * @author RollW
 */
object DatabaseConfigKeys : SettingSpecificationSupplier {
    const val PREFIX = "database."

    private val LOCAL_SOURCE = listOf(SettingSource.LOCAL)

    // TODO: add description when AttributedSettingSpec is ready

    @JvmField
    val DATABASE_URL =
        SettingSpecificationBuilder(SettingKey.ofString("database.url"))
            .setDescription(SettingDescription.text("Database URL"))
            .setSupportedSources(LOCAL_SOURCE)
            .setDefaultValue(null)
            .setRequired(true)
            .build()
    @JvmField
    val DATABASE_USERNAME =
        SettingSpecificationBuilder(SettingKey.ofString("database.username"))
            .setDescription(SettingDescription.text("Database username"))
            .setDefaultValue(null)
            .setSupportedSources(LOCAL_SOURCE)
            .setRequired(false)
            .build()

    @JvmField
    val DATABASE_PASSWORD =
        SettingSpecificationBuilder(SettingKey.ofString("database.password"))
            .setDescription(SettingDescription.text("Database password"))
            .setDefaultValue(null)
            .setSupportedSources(LOCAL_SOURCE)
            .setRequired(false)
            .build()

    private val keys = listOf(
        DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD
    )

    override val specifications: List<AttributedSettingSpecification<*, *>>
        get() = keys
}