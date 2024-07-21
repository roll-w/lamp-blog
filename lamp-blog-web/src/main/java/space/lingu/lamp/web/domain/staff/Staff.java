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

import space.lingu.NonNull;
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author RollW
 */
@SuppressWarnings({"ClassCanBeRecord", "unused"})
@DataTable(name = "staff")
public class Staff implements LongDataItem<Staff> {
    // TODO: staff type

    @DataColumn(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final Long id;

    @DataColumn(name = "user_id")
    private final long userId;

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

    public Staff(Long id, long userId,
                 Set<StaffType> types, long createTime,
                 long updateTime, boolean allowUser,
                 boolean deleted) {
        this.id = id;
        this.userId = userId;
        this.types = EnumSet.copyOf(types);
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.allowUser = allowUser;
        this.deleted = deleted;
    }

    @Override
    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public Set<StaffType> getTypes() {
        return types;
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    @Override
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

    @Override
    @NonNull
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.STAFF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Staff staff)) return false;
        return userId == staff.userId && createTime == staff.createTime && updateTime == staff.updateTime && allowUser == staff.allowUser && deleted == staff.deleted && Objects.equals(id, staff.id) && Objects.equals(types, staff.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, types, createTime, updateTime, allowUser, deleted);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", userId=" + userId +
                ", types=" + types +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", allowUser=" + allowUser +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public final static class Builder implements LongEntityBuilder<Staff> {
        private Long id;
        private long userId;
        private Set<StaffType> type;
        private long createTime;
        private long updateTime;
        private boolean allowUser;
        private boolean deleted;

        public Builder() {
        }

        public Builder(Staff staff) {
            this.id = staff.id;
            this.userId = staff.userId;
            this.type = staff.types;
            this.createTime = staff.createTime;
            this.updateTime = staff.updateTime;
            this.allowUser = staff.allowUser;
            this.deleted = staff.deleted;
        }

        @Override
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
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

        @Override
        public Staff build() {
            return new Staff(id, userId, type, createTime,
                    updateTime, allowUser, deleted);
        }
    }
}
