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

import space.lingu.lamp.DataItem;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author RollW
 */
@SuppressWarnings({"ClassCanBeRecord", "unused"})
@DataTable(name = "staff")
public class Staff implements DataItem {
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

    @DataColumn(name = "allow_user")
    private final boolean allowUser;

    @DataColumn(name = "deleted")
    private final boolean deleted;

    public Staff(long userId, String employeeId,
                 Set<StaffType> types, long createTime,
                 long updateTime, boolean allowUser,
                 boolean deleted) {
        this.userId = userId;
        this.employeeId = employeeId;
        this.types = EnumSet.copyOf(types);
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.allowUser = allowUser;
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

    public boolean isAllowUser() {
        return allowUser;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public boolean hasType(StaffType type) {
        return types.contains(type);
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return userId == staff.userId && createTime == staff.createTime && updateTime == staff.updateTime && allowUser == staff.allowUser && deleted == staff.deleted && Objects.equals(employeeId, staff.employeeId) && Objects.equals(types, staff.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, employeeId, types, createTime, updateTime, allowUser, deleted);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "userId=" + userId +
                ", employeeId='" + employeeId + '\'' +
                ", types=" + types +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", allowUser=" + allowUser +
                ", deleted=" + deleted +
                '}';
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
        private boolean allowUser;
        private boolean deleted;

        public Builder() {
        }

        public Builder(Staff staff) {
            this.userId = staff.userId;
            this.employeeId = staff.employeeId;
            this.type = staff.types;
            this.createTime = staff.createTime;
            this.updateTime = staff.updateTime;
            this.allowUser = staff.allowUser;
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

        public Builder setAllowUser(boolean allowUser) {
            this.allowUser = allowUser;
            return this;
        }

        public Builder setDeleted(boolean deleted) {
            this.deleted = deleted;
            return this;
        }

        public Staff build() {
            return new Staff(userId, employeeId, type, createTime,
                    updateTime, allowUser, deleted);
        }
    }
}
