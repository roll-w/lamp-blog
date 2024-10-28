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

package space.lingu.lamp.web.database;

import space.lingu.lamp.web.database.dao.UserPersonalDataDao;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.light.DataConverters;
import space.lingu.light.Database;
import space.lingu.light.LightConfiguration;
import space.lingu.light.LightDatabase;

/**
 * @author RollW
 */
@Database(name = "lamp_blog_database", version = 1, tables = {
        UserPersonalData.class
})
@DataConverters({LampConverter.class})
@LightConfiguration(key = LightConfiguration.KEY_VARCHAR_LENGTH, value = "255")
public abstract class LampDatabase extends LightDatabase {
    public abstract UserPersonalDataDao getUserPersonalDataDao();
}
