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

package space.lingu.lamp.web.system.entity;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;
import space.lingu.light.SQLDataType;

import java.util.Locale;

/**
 * Message resource, overrides the default message resource in system.
 * Could be used to override the default i18n messages in system.
 *
 * @author RollW
 */
@DataTable(tableName = "message_resource")
public record MessageResource(
        @DataColumn(name = "key")
        @PrimaryKey
        String key,

        @DataColumn(name = "value", dataType = SQLDataType.LONGTEXT)
        String value,

        @DataColumn(name = "locale")
        @PrimaryKey
        Locale locale
) {
}
