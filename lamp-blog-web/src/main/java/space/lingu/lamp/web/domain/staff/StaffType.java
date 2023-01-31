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

package space.lingu.lamp.web.domain.staff;

/**
 * @author RollW
 */
public enum StaffType {
    /**
     * Administrator.
     */
    ADMIN,
    /**
     * Reviewer.
     *
     * @see space.lingu.lamp.web.domain.review.ReviewJob
     */
    REVIEWER,
    /**
     * Customer service.
     */
    CUSTOMER_SERVICE,
    /**
     * Editor.
     */
    EDITOR,
    ;

    public static StaffType of(String name) {
        for (StaffType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such staff type: " + name);
    }
}
