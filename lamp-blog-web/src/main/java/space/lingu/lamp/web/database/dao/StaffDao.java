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

package space.lingu.lamp.web.database.dao;

import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Query;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public interface StaffDao extends AutoPrimaryBaseDao<Staff> {
    @Override
    @Query("SELECT * FROM staff WHERE id = {id}")
    Staff getById(long id);

    @Override
    @Query("SELECT * FROM staff WHERE id IN ({ids})")
    List<Staff> getByIds(List<Long> ids);

    @Override
    @Query("SELECT * FROM staff ORDER BY id DESC")
    List<Staff> get();

    @Override
    @Query("SELECT COUNT(*) FROM staff")
    int count();

    @Override
    @Query("SELECT * FROM staff ORDER BY id DESC LIMIT {offset.limit()} OFFSET {offset.offset()}")
    List<Staff> get(Offset offset);

    @Override
    default String getTableName() {
        return "staff";
    }

    @Query("SELECT * FROM staff WHERE user_id = {userId}")
    Staff getByUserId(long userId);

    @Delete("UPDATE staff SET deleted = {deleted}, update_time = {time} WHERE id = {id}")
    void setStaffDeleted(long id, boolean deleted, long time);

    @Query("SELECT * FROM staff WHERE types LIKE CONCAT('%', {type}, '%')")
    List<Staff> getStaffsOfType(StaffType type);

    @Query("SELECT * FROM staff WHERE types LIKE CONCAT('%', {type}, '%')")
    List<Staff> getStaffsOfType(StaffType type, boolean deleted);

    @Query("SELECT COUNT(*) FROM staff")
    int getStaffsCount();
}

