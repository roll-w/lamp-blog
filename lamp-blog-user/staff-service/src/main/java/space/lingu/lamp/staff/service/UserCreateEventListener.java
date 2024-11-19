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

package space.lingu.lamp.staff.service;

import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.lingu.NonNull;
import space.lingu.lamp.staff.StaffType;
import space.lingu.lamp.staff.persistence.StaffDo;
import space.lingu.lamp.staff.persistence.StaffRepository;
import space.lingu.lamp.user.AttributedUser;
import space.lingu.lamp.user.Role;
import space.lingu.lamp.user.event.OnUserCreateEvent;

import java.time.OffsetDateTime;

/**
 * @author RollW
 */
@Component
public class UserCreateEventListener implements ApplicationListener<OnUserCreateEvent> {

    private final StaffRepository staffRepository;

    public UserCreateEventListener(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    @Async
    public void onApplicationEvent(@NonNull OnUserCreateEvent event) {
        AttributedUser attributedUser = event.getUser();
        if (attributedUser.getRole() == Role.USER) {
            return;
        }
        OffsetDateTime now = OffsetDateTime.now();
        StaffType type = StaffType.of(attributedUser.getRole());
        StaffDo staff = StaffDo.builder()
                .setUserId(attributedUser.getUserId())
                .setCreateTime(now)
                .setUpdateTime(now)
                .addTypes(type)
                .setDeleted(false)
                .build();
        staffRepository.save(staff);
    }
}
