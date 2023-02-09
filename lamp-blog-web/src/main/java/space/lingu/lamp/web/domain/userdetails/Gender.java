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

package space.lingu.lamp.web.domain.userdetails;

/**
 * @author RollW
 */
public enum Gender {
    MALE("gender.male"),
    FEMALE("gender.female"),
    /**
     * Private or unknown.
     */
    PRIVATE("gender.private"),
    ;

    private final String propertiesName;

    Gender(String propertiesName) {
        this.propertiesName = propertiesName;
    }

    public String getPropertiesName() {
        return propertiesName;
    }

    public static Gender of(String value) {
        for (Gender gender : values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        return PRIVATE;
    }

    public static Gender of(Object o) {
        if (o instanceof Gender) {
            return (Gender) o;
        }
        if (o instanceof String) {
            return of((String) o);
        }
        return of(o.toString());
    }
}
