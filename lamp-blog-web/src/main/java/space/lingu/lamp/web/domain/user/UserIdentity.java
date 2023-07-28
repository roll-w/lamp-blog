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

package space.lingu.lamp.web.domain.user;

import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import tech.rollw.common.web.system.Operator;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceKind;

/**
 * @author RollW
 */
public interface UserIdentity extends Operator, SystemResource<Long> {
    long getUserId();

    String getUsername();

    String getEmail();

    Role getRole();

    @Override
    default long getOperatorId() {
        return getUserId();
    }

    @Override
    default Long getResourceId() {
        return getUserId();
    }

    @Override
    default SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.USER;
    }
}
