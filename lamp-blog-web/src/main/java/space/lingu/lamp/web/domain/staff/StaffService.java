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

import space.lingu.lamp.data.page.Page;
import space.lingu.lamp.data.page.Pageable;
import space.lingu.lamp.web.domain.staff.dto.StaffInfo;

import java.util.List;

/**
 * @author RollW
 */
public interface StaffService {
    // TODO: optimize methods.

    Page<StaffInfo> getStaffs(Pageable pageable);

    List<Staff> getStaffs();

    List<Staff> getStaffsByType(StaffType type, int page, int size);

    Staff getStaff(long userId);

    Staff createStaff(Staff staff);

    Staff updateStaff(Staff staff);

    void deleteStaff(String staffId);

    void forbiddenStaff(String staffId);

    void restoreStaff(String staffId);

}