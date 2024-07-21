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

import space.lingu.NonNull;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.lamp.web.domain.user.UserIdentity;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.Set;

/**
 * @author RollW
 */
public record StaffInfo(
        long id,
        long userId,
        UserIdentity userIdentity,
        Set<StaffType> types,
        long createTime,
        long updateTime,
        boolean allowUser,
        boolean deleted
) implements SystemResource<Long> {
    public static StaffInfo from(Staff staff, UserIdentity userIdentity) {
        if (staff == null) {
            return null;
        }
        return new StaffInfo(
                staff.getId(),
                staff.getUserId(),
                userIdentity,
                staff.getTypes(),
                staff.getCreateTime(),
                staff.getUpdateTime(),
                staff.isAllowUser(),
                staff.isDeleted()
        );
    }

    @Override
    public Long getResourceId() {
        return id;
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.STAFF;
    }
}
