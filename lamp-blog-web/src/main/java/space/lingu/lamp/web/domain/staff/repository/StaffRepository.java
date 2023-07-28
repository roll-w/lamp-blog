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

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.StaffDao;
import space.lingu.lamp.web.database.repo.AutoPrimaryBaseRepository;
import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.staff.StaffType;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class StaffRepository extends AutoPrimaryBaseRepository<Staff> {
    private final StaffDao staffDao;

    public StaffRepository(LampDatabase lampDatabase,
                           ContextThreadAware<PageableContext> pageableContextThreadAware,
                           CacheManager cacheManager) {
        super(lampDatabase.getStaffDao(), pageableContextThreadAware, cacheManager);
        this.staffDao = lampDatabase.getStaffDao();
    }

    @Override
    protected Class<Staff> getEntityClass() {
        return Staff.class;
    }

    public List<Staff> getStaffsByType(StaffType type) {
        return cacheResult(
                staffDao.getStaffsOfType(type)
        );
    }

    public List<Staff> getActiveStaffsByType(StaffType type) {
        return cacheResult(
                staffDao.getStaffsOfType(type, false)
        );
    }

    public void deleteByUserId(long userId, long updateTime) {
        staffDao.setStaffDeleted(userId, true, updateTime);
    }

    public int countOfType(StaffType type) {
        return staffDao.getStaffsCount();
    }

    public Staff getByUserId(long userId) {
        return cacheResult(staffDao.getByUserId(userId));
    }
}