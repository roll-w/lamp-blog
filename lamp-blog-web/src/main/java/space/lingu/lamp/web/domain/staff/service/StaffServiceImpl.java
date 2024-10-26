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

package space.lingu.lamp.web.domain.staff.service;

import org.apache.commons.lang3.Validate;
import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.staff.Staff;
import space.lingu.lamp.web.domain.staff.StaffService;
import space.lingu.lamp.web.domain.staff.StaffType;
import space.lingu.lamp.web.domain.staff.StaffInfo;
import space.lingu.lamp.web.domain.staff.repository.StaffRepository;
import space.lingu.lamp.user.UserIdentity;
import space.lingu.lamp.user.UserProvider;
import space.lingu.lamp.user.UserSearchService;
import tech.rollw.common.web.CommonRuntimeException;
import tech.rollw.common.web.DataErrorCode;
import tech.rollw.common.web.page.ImmutablePage;
import tech.rollw.common.web.page.Page;
import tech.rollw.common.web.page.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
@Service
public class StaffServiceImpl implements StaffService {
    private final StaffRepository staffRepository;
    private final UserSearchService userSearchService;
    private final UserProvider userProvider;

    public StaffServiceImpl(StaffRepository staffRepository,
                            UserSearchService userSearchService,
                            UserProvider userProvider) {
        this.staffRepository = staffRepository;
        this.userSearchService = userSearchService;
        this.userProvider = userProvider;
    }

    @Override
    public Page<StaffInfo> getStaffs(Pageable pageable) {
        List<Staff> staffs = staffRepository.get(pageable.toOffset());
        List<Long> ids = staffs.stream()
                .map(Staff::getUserId)
                .toList();
        List<? extends UserIdentity> userIdentities = userSearchService
                .findUsers(ids);
        List<StaffInfo> staffInfos = pairByUserId(staffs, userIdentities);
        return ImmutablePage.of(
                pageable.getPage(),
                pageable.getSize(),
                1, // TODO: get total page
                staffInfos
        );
    }

    private List<StaffInfo> pairByUserId(List<Staff> staffs,
                                         List<? extends UserIdentity> userIdentities) {
        List<StaffInfo> staffInfos = new ArrayList<>();
        staffs.forEach(staff -> userIdentities
                .stream()
                .filter(userIdentity -> userIdentity.getUserId() == staff.getUserId())
                .findFirst()
                .ifPresent(userIdentity ->
                        staffInfos.add(StaffInfo.from(staff, userIdentity)))
        );
        return staffInfos;
    }

    @Override
    public Page<StaffInfo> getStaffsByType(StaffType type, Pageable pageable) {

        return null;
    }

    @Override
    public StaffInfo getStaffByUser(long userId) {
        Staff staff = staffRepository.getByUserId(userId);
        if (staff == null) {
            throw new CommonRuntimeException(DataErrorCode.ERROR_DATA_NOT_EXIST);
        }
        UserIdentity userIdentity = userProvider.getUser(userId);
        return StaffInfo.from(staff, userIdentity);
    }

    @Override
    public StaffInfo getStaff(long staffId) {
        Staff staff = staffRepository.getById(staffId);
        if (staff == null) {
            throw new CommonRuntimeException(DataErrorCode.ERROR_DATA_NOT_EXIST);
        }
        UserIdentity userIdentity = userProvider.getUser(staff.getUserId());
        return StaffInfo.from(staff, userIdentity);
    }

    @Override
    public Staff createStaff(Staff staff) {
        Validate.notNull(staff, "staff must not be null.");
        staffRepository.insert(staff);
        return null;
    }

    @Override
    public void updateStaff(Staff staff) {
        staffRepository.update(staff);
    }

    @Override
    public void deleteStaff(String staffId) {

    }

    @Override
    public void forbiddenStaff(String staffId) {

    }

    @Override
    public void restoreStaff(String staffId) {

    }
}
