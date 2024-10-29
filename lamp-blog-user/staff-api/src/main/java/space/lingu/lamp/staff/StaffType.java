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

package space.lingu.lamp.staff;

import space.lingu.lamp.user.Role;

/**
 * For job assignment, we need to know the type of staff.
 *
 * @author RollW
 */
public enum StaffType {
    /**
     * Administrator.
     */
    ADMIN(3),
    /**
     * Unassigned.
     */
    UNASSIGNED(1),
    /**
     * Reviewer.
     */
    REVIEWER(1),
    /**
     * Customer service.
     */
    CUSTOMER_SERVICE(1),
    /**
     * Editor.
     */
    EDITOR(1),
    ;

    /**
     * When staff type changes, which staff type has higher
     * priority will be assigned user's role.
     */
    private final int priority;

    StaffType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static StaffType of(String name) {
        for (StaffType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No such staff type: " + name);
    }

    public static StaffType of(Role role) {
        return switch (role) {
            case ADMIN -> ADMIN;
            case REVIEWER -> REVIEWER;
            case CUSTOMER_SERVICE -> CUSTOMER_SERVICE;
            case EDITOR -> EDITOR;
            case STAFF -> UNASSIGNED;
            default -> throw new IllegalArgumentException("No such staff type: " + role);
        };
    }
}
