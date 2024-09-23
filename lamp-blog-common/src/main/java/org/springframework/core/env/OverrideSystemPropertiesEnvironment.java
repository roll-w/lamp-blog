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

package org.springframework.core.env;

import space.lingu.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link StandardEnvironment} subclass that allows for turning
 * off the resolution of system properties and environment variables.
 *
 * @author RollW
 */
public class OverrideSystemPropertiesEnvironment extends StandardEnvironment {
    private final boolean allowSystemProperties;
    private final boolean allowSystemEnvironment;

    public OverrideSystemPropertiesEnvironment(boolean allowSystemProperties, boolean allowSystemEnvironment) {
        this.allowSystemProperties = allowSystemProperties;
        this.allowSystemEnvironment = allowSystemEnvironment;
    }

    @NonNull
    @Override
    public Map<String, Object> getSystemProperties() {
        if (allowSystemProperties) {
            return super.getSystemProperties();
        }
        return new HashMap<>();
    }

    @NonNull
    @Override
    public Map<String, Object> getSystemEnvironment() {
        if (allowSystemEnvironment) {
            return super.getSystemEnvironment();
        }
        return new HashMap<>();
    }
}
