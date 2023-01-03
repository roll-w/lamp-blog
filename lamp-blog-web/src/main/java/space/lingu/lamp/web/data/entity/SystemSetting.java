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

package space.lingu.lamp.web.data.entity;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

/**
 * @author RollW
 */
@DataTable(tableName = "system_setting")
@SuppressWarnings("all")
public class SystemSetting {
    @DataColumn(name = "key")
    @PrimaryKey
    private final String key;

    @DataColumn(name = "value")
    private final String value;

    public SystemSetting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
