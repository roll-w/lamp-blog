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

import space.lingu.lamp.setting.SettingKey
import space.lingu.lamp.setting.SimpleSettingSpec

/**
 * @author RollW
 */
object DatabaseConfigKeys {
    const val PREFIX = "database"

    // TODO: add description when AttributedSettingSpec is ready

    @JvmField
    val DATABASE_URL = SimpleSettingSpec(
        SettingKey.ofString("database.url"), default = null
    )

    @JvmField
    val DATABASE_USERNAME = SimpleSettingSpec(
        SettingKey.ofString("database.username"), default = null
    )

    @JvmField
    val DATABASE_PASSWORD = SimpleSettingSpec(
        SettingKey.ofString("database.password"), default = null
    )
}