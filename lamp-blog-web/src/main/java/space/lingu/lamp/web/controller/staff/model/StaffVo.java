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

package space.lingu.lamp.web.controller.staff.model;

import space.lingu.lamp.user.Role;
import space.lingu.lamp.web.domain.staff.StaffInfo;
import space.lingu.lamp.web.domain.staff.StaffType;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author RollW
 */
public record StaffVo(
        long id,
        long userId,
        String username,
        Role role,
        Set<StaffType> types,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean allowUser,
        boolean deleted
) {

    public static StaffVo from(StaffInfo staffInfo) {
        if (staffInfo == null) {
            return null;
        }
        return new StaffVo(
                staffInfo.getId(),
                staffInfo.getUserId(),
                staffInfo.getUserIdentity().getUsername(),
                staffInfo.getUserIdentity().getRole(),
                staffInfo.getTypes(),
                staffInfo.getCreateTime(),
                staffInfo.getUpdateTime(),
                staffInfo.getAllowUser(),
                staffInfo.getDeleted()
        );
    }
}
