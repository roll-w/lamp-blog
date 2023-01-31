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

package space.lingu.lamp.web.domain.content;

import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.dto.UserInfo;

import java.util.Arrays;

/**
 * Content access authorization type.
 *
 * @author RollW
 */
public enum ContentAccessAuthType {
    /**
     * None or public.
     */
    PUBLIC(),
    /**
     * Needs to be logged in and has the password.
     * <p>
     * String: password.
     */
    PASSWORD(String.class),
    /**
     * Private content.
     * <p>
     * Long: user id.
     */
    PRIVATE(Long.class, UserInfo.class, User.class),
    /**
     * Needs to be logged in.
     * <p>
     * Long: user id.
     */
    USER(Long.class, UserInfo.class, User.class),
    /**
     * Needs to be logged in and in the group.
     * <p>
     * Long: user id.
     */
    USER_GROUP(Long.class, UserInfo.class, User.class),
    ;

    // types of data that can be used to authenticate.
    // basically as a hint for the front end.
    private final Class<?>[] types;

    ContentAccessAuthType(Class<?>... types) {
        this.types = types;
    }

    /**
     * Get the types of data that can be used to authenticate.
     *
     * @return supported types.
     */
    public Class<?>[] getTypes() {
        return types;
    }

    public boolean needsCheck() {
        return types.length > 0;
    }

    public boolean needsAuth() {
        return this != PUBLIC;
    }

    public static void checkTypeMatches(ContentAccessAuthType type, Object data) {
        if (data == null) {
            return;
        }
        if (!type.needsCheck()) {
            return;
        }
        for (Class<?> typeType : type.getTypes()) {
            if (typeType.isAssignableFrom(data.getClass())) {
                return;
            }
        }
        throw new IllegalArgumentException("Data type is not assignable to any of the types in ContentAccessAuthType: " + type +
                ", supports: " + Arrays.toString(type.getTypes()) + ", data type: " + data.getClass());
    }

}
