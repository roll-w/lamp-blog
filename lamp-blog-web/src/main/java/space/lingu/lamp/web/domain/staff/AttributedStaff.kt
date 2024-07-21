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

import space.lingu.lamp.TimeAttributed
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind
import tech.rollw.common.web.system.SystemResource
import tech.rollw.common.web.system.SystemResourceKind

/**
 * @author RollW
 */
interface AttributedStaff : SystemResource<Long>, TimeAttributed {
    val staffId: Long

    val userId: Long

    val types: Set<StaffType>

    override fun getResourceId(): Long = staffId

    override fun getSystemResourceKind(): SystemResourceKind =
        LampSystemResourceKind.STAFF

    companion object {
        @JvmStatic
        fun AttributedStaff.isType(type: StaffType): Boolean {
            return types.contains(type)
        }
    }
}