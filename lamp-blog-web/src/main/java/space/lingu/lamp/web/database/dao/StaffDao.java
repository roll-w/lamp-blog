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

import space.lingu.Dangerous;
import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.light.Dao;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Query;
import space.lingu.light.Update;

import java.util.List;

/**
 * @author RollW
 */
@Dao
public abstract class StaffDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(Staff... staffs);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    public abstract void insert(List<Staff> staffs);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(Staff... staffs);

    @Update(onConflict = OnConflictStrategy.ABORT)
    public abstract void update(List<Staff> staffs);

    @Delete
    @Dangerous(message = "This method will delete data completely in the table. Use with caution.")
    protected abstract void delete(Staff Staff);

    @Delete
    @Dangerous(message = "This method will delete data completely in the table. Use with caution.")
    protected abstract void delete(List<Staff> staffs);

    @Delete("DELETE FROM staff")
    @Dangerous(message = "This method will delete all data in the table. Use with caution.")
    protected abstract void clearTable();

    @Query("SELECT * FROM staff")
    public abstract List<Staff> get();

    @Query("SELECT * FROM staff WHERE types LIKE '%{type}%'")
    public abstract List<Staff> getStaffOfType(StaffType type);

    @Query("SELECT * FROM staff WHERE types REGEXP '{type1}|{type2}'")
    public abstract List<Staff> getStaffOfTypes(StaffType type1, StaffType type2);
}

