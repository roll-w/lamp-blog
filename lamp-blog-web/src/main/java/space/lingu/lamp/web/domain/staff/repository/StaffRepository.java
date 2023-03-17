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

package space.lingu.lamp.web.domain.staff.repository;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.data.page.Pageable;
import space.lingu.lamp.web.common.CacheNames;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.StaffDao;
import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.staff.StaffType;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class StaffRepository {
    private final StaffDao staffDao;
    private final Cache staffCache;

    public StaffRepository(LampDatabase database, CacheManager cacheManager) {
        staffDao = database.getStaffDao();
        staffCache = cacheManager.getCache(CacheNames.STAFFS);
    }

    public void insert(Staff staff) {
        staffDao.insert(staff);
        staffCache.put(staff.getUserId(), staff);
    }

    public void update(Staff staff) {
        staffDao.update(staff);
        staffCache.put(staff.getUserId(), staff);
    }

    public Staff getById(long userId) {
        Staff staff = staffCache.get(userId, Staff.class);
        if (staff == null) {
            staff = staffDao.getById(userId);
            staffCache.put(userId, staff);
        }
        return staff;
    }


    public Staff getByStaffId(String employeeId) {
        Staff staff = staffCache.get(employeeId, Staff.class);
        if (staff == null) {
            staff = staffDao.getByStaffId(employeeId);
            staffCache.put(employeeId, staff);
        }
        return staff;
    }

    public List<Staff> getStaffs(Pageable pageable) {
        return staffDao.get(pageable.toOffset());
    }

    public List<Staff> getStaffs() {
        return staffDao.get();
    }

    public List<Staff> getStaffsByType(StaffType type) {
        return staffDao.getStaffsOfType(type);
    }

    public List<Staff> getActiveStaffsByType(StaffType type) {
        return staffDao.getStaffsOfType(type, false);
    }

    public void deleteByUserId(long userId, long updateTime) {
        staffDao.setStaffDeleted(userId, true, updateTime);
        staffCache.evict(userId);
    }

    public int countOfType(StaffType type) {
        return staffDao.getStaffsCount();
    }
}
