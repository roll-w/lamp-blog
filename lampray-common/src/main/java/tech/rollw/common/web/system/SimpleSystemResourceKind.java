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

package tech.rollw.common.web.system;

import java.util.List;

/**
 * @author RollW
 */
public class SimpleSystemResourceKind implements SystemResourceKind {
    private final String name;
    private final List<String> aliases;

    public SimpleSystemResourceKind(String name, String... aliases) {
        this.name = name;
        this.aliases = List.of(aliases);
    }

    public SimpleSystemResourceKind(String name, List<String> aliases) {
        this.name = name;
        this.aliases = aliases;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }
}
