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

import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.lamp.web.domain.userdetails.Birthday;
import space.lingu.light.DataConverter;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringJoiner;

/**
 * @author RollW
 */
public class LampConverter {
    @DataConverter
    public static String toLocaleString(Locale locale) {
        return locale.toLanguageTag();
    }

    @DataConverter
    public static Locale toLocale(String localeString) {
        return Locale.forLanguageTag(localeString);
    }

    @DataConverter
    public static Set<StaffType> convertToStaffType(String values) {
        Set<StaffType> staffTypes = EnumSet.noneOf(StaffType.class);
        for (String value : values.split(",")) {
            staffTypes.add(StaffType.of(value));
        }
        return staffTypes;
    }

    @DataConverter
    public static String convertStaffTypeSet(Set<StaffType> staffTypes) {
        StringJoiner joiner = new StringJoiner(",");
        staffTypes.forEach(type -> joiner.add(type.name()));
        return joiner.toString();
    }

    @DataConverter
    public static String convertBirthday(Birthday birthday) {
        return birthday.getBirthday();
    }

    @DataConverter
    public static Birthday convertToBirthday(String birthday) {
        return Birthday.fromString(birthday);
    }

    private LampConverter() {
    }
}
