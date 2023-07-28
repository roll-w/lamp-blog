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
import space.lingu.lamp.web.domain.user.AttributedUser;
import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.event.OnUserCreateEvent;

/**
 * @author RollW
 */
@Component
public class UserCreateEventListener implements ApplicationListener<OnUserCreateEvent> {
    private final StaffRepository repository;

    public UserCreateEventListener(StaffRepository repository) {
        this.repository = repository;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnUserCreateEvent event) {
        AttributedUser attributedUser = event.getUser();
        if (attributedUser.getRole() == Role.USER) {
            return;
        }
        StaffType type = StaffType.of(attributedUser.getRole());
        long time = System.currentTimeMillis();
        Staff staff = Staff.builder()
                .setUserId(attributedUser.getUserId())
                .setTypes(type)
                .setAllowUser(true)
                .setCreateTime(time)
                .setUpdateTime(time)
                .setDeleted(false)
                .build();
        repository.insert(staff);
    }

}
