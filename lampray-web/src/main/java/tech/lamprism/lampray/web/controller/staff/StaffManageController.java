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

package tech.lamprism.lampray.web.controller.staff;

import org.springframework.web.bind.annotation.GetMapping;
import tech.lamprism.lampray.staff.StaffService;
import tech.lamprism.lampray.web.controller.AdminApi;
import tech.lamprism.lampray.web.controller.staff.model.StaffVo;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.page.Pageable;

import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class StaffManageController {
    private final StaffService staffService;

    public StaffManageController(StaffService staffService) {
        this.staffService = staffService;
    }

    @GetMapping("/staffs")
    public HttpResponseEntity<List<StaffVo>> getStaffs(
            Pageable pageable) {
        return HttpResponseEntity.success();
    }

}
