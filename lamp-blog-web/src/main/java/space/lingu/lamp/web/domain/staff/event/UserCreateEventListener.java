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
import space.lingu.lamp.web.domain.staff.StaffSerialNumberGenerator;
import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.lamp.web.domain.staff.repository.StaffRepository;
import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.dto.UserInfo;
import space.lingu.lamp.web.domain.user.event.OnUserCreateEvent;

/**
 * @author RollW
 */
@Component
public class UserCreateEventListener implements ApplicationListener<OnUserCreateEvent> {
    private final StaffRepository repository;
    private final StaffSerialNumberGenerator serialNumberGenerator;

    public UserCreateEventListener(StaffRepository repository,
                                   StaffSerialNumberGenerator serialNumberGenerator) {
        this.repository = repository;
        this.serialNumberGenerator = serialNumberGenerator;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnUserCreateEvent event) {
        UserInfo userInfo = event.getUserInfo();
        if (userInfo.role() == Role.USER) {
            return;
        }
        StaffType type = StaffType.of(userInfo.role());
        String employeeId = serialNumberGenerator.generate(userInfo.id(), type);
        long time = System.currentTimeMillis();
        Staff staff = Staff.builder()
                .setUserId(userInfo.id())
                .setTypes(type)
                .setAllowUser(true)
                .setEmployeeId(employeeId)
                .setCreateTime(time)
                .setDeleted(false)
                .build();
        repository.insert(staff);
    }

}
