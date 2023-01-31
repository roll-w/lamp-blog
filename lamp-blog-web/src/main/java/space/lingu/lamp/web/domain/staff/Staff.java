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

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

import java.util.EnumSet;
import java.util.Set;

/**
 * @author RollW
 */
@SuppressWarnings({"ClassCanBeRecord", "unused"})
@DataTable(tableName = "staff")
public class Staff {
    // TODO: staff type
    @DataColumn(name = "id")
    @PrimaryKey
    private final long userId;

    @DataColumn(name = "employee_id")
    private final String employeeId;

    @DataColumn(name = "types")
    private final Set<StaffType> types;

    @DataColumn(name = "create_time")
    private final long createTime;

    @DataColumn(name = "update_time")
    private final long updateTime;

    @DataColumn(name = "deleted")
    private final boolean deleted;

    public Staff(long userId, String employeeId,
                 Set<StaffType> types, long createTime,
                 long updateTime, boolean deleted) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.types = EnumSet.copyOf(types);
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.deleted = deleted;
    }

    public long getUserId() {
        return userId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Set<StaffType> getTypes() {
        return types;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Builder toBuilder() {
        return new Builder();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long userId;
        private String employeeId;
        private Set<StaffType> type;
        private long createTime;
        private long updateTime;
        private boolean deleted;

        public Builder() {
        }

        public Builder(Staff staff) {
            this.userId = staff.userId;
            this.employeeId = staff.employeeId;
            this.type = staff.types;
            this.createTime = staff.createTime;
            this.updateTime = staff.updateTime;
            this.deleted = staff.deleted;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setEmployeeId(String employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder setTypes(StaffType... types) {
            this.type = EnumSet.of(types[0], types);
            return this;
        }

        public Builder addType(StaffType type) {
            if (this.type == null) {
                this.type = EnumSet.of(type);
                return this;
            }
            this.type.add(type);
            return this;
        }

        public Builder setCreateTime(long createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Staff build() {
            return new Staff(userId, employeeId, type, createTime, updateTime, deleted);
        }
    }
}
