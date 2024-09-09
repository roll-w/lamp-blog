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

package space.lingu.lamp.web.domain.staff.event;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.lamp.web.domain.staff.repository.StaffRepository;
import space.lingu.lamp.user.Role;
import space.lingu.lamp.user.UserInfo;
import space.lingu.lamp.user.event.OnUserRoleChangeEvent;

/**
 * @author RollW
 */
@Component
public class UserRoleChangeListener
        implements ApplicationListener<OnUserRoleChangeEvent> {
    private final StaffRepository staffRepository;

    public UserRoleChangeListener(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnUserRoleChangeEvent event) {
        UserInfo userInfo = event.getUserInfo();
        Staff staff = staffRepository.getByUserId(userInfo.getUserId());
        if (event.getCurrentRole() == Role.USER) {
            long time = System.currentTimeMillis();
            disableStaff(staff, time);
            return;
        }
        StaffType type = StaffType.of(userInfo.role());
        long time = System.currentTimeMillis();
        Staff newStaff = Staff.builder()
                .setUserId(userInfo.id())
                .setTypes(type)
                .setAllowUser(true)
                .setCreateTime(time)
                .setUpdateTime(time)
                .setDeleted(false)
                .build();
        staffRepository.insert(newStaff);
    }

    private void disableStaff(Staff staff, long time) {
        if (staff == null) {
            return;
        }
        staffRepository.deleteByUserId(staff.getUserId(), time);
    }
}
