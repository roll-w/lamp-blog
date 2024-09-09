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

package space.lingu.lamp.web.domain.staff

import space.lingu.lamp.user.UserIdentity

/**
 * @author RollW
 */
data class StaffInfo(
    val id: Long,
    val userIdentity: UserIdentity,
    override val types: Set<StaffType>,
    private val createTime: Long,
    private val updateTime: Long,
    val allowUser: Boolean,
    val deleted: Boolean
) : AttributedStaff {
    override val staffId: Long
        get() = id

    override val userId: Long
        get() = userIdentity.userId

    override fun getCreateTime(): Long = createTime

    override fun getUpdateTime(): Long = updateTime

    companion object {
        @JvmStatic
        fun from(staff: Staff, userIdentity: UserIdentity): StaffInfo {
            return StaffInfo(
                staff.id,
                userIdentity,
                staff.types,
                staff.createTime,
                staff.updateTime,
                staff.isAllowUser,
                staff.isDeleted
            )
        }

        @JvmStatic
        fun Staff.toStaffInfo(userIdentity: UserIdentity): StaffInfo {
            return from(this, userIdentity)
        }
    }
}