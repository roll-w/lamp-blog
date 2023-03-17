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

package space.lingu.lamp.web.domain.staff.service;

import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.staff.StaffSerialNumberGenerator;
import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.lamp.web.domain.staff.repository.StaffRepository;

/**
 * @author RollW
 */
@Service
public class StaffSerialNumberImpl implements StaffSerialNumberGenerator {
    private final StaffRepository staffRepository;

    public StaffSerialNumberImpl(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public String generate(long userId, StaffType staffType) {
        String prefix = prefix(staffType);
        int serial = count(staffType) + 1;
        String to6Digit = String.format("%06d", serial);
        return prefix + to6Digit;
    }

    private int count(StaffType type) {
        return staffRepository.countOfType(type);
    }

    private String prefix(StaffType staffType) {
        return switch (staffType) {
            case ADMIN -> "AM";
            case REVIEWER -> "RV";
            case EDITOR -> "ET";
            case UNASSIGNED -> "UA";
            case CUSTOMER_SERVICE -> "CS";
        };
    }
}
