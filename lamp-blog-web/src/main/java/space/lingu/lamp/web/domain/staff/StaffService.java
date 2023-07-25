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

import space.lingu.lamp.web.domain.staff.dto.StaffInfo;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;

/**
 * @author RollW
 */
public interface StaffService {
    // TODO: optimize methods.

    Page<StaffInfo> getStaffs(Pageable pageable);

    Page<StaffInfo> getStaffsByType(StaffType type, Pageable pageable);

    StaffInfo getStaffByUser(long userId);

    StaffInfo getStaff(String staffId);

    Staff createStaff(Staff staff);

    void updateStaff(Staff staff);

    void deleteStaff(String staffId);

    void forbiddenStaff(String staffId);

    void restoreStaff(String staffId);

}
