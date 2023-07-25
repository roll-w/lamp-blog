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

package space.lingu.lamp.web.controller.user;

import org.springframework.web.bind.annotation.*;
import space.lingu.lamp.web.controller.AdminApi;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.service.UserManageService;
import space.lingu.lamp.web.domain.user.vo.UserDetailsVo;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.lamp.web.domain.userdetails.UserPersonalDataService;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.page.Pageable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class UserManageController {
    private final UserManageService userManageService;
    private final UserPersonalDataService userPersonalDataService;

    public UserManageController(UserManageService userManageService,
                                UserPersonalDataService userPersonalDataService) {
        this.userManageService = userManageService;
        this.userPersonalDataService = userPersonalDataService;
    }

    @GetMapping("/users")
    public HttpResponseEntity<List<UserDetailsVo>> getUserList(Pageable pageable) {
        List<User> userIdentities = userManageService.getUsers(
                pageable.getPage(),
                pageable.getSize()
        );
        List<UserPersonalData> personalDataList =
                userPersonalDataService.getPersonalData(userIdentities);
        List<UserDetailsVo> userDetailsVos = new ArrayList<>();
        for (User user : userIdentities) {
            UserPersonalData personalData = getPersonalData(user, personalDataList);
            userDetailsVos.add(UserDetailsVo.of(user, personalData));
        }
        return HttpResponseEntity.success(userDetailsVos);
    }

    private UserPersonalData getPersonalData(User user,
                                             List<UserPersonalData> personalDataList) {
        for (UserPersonalData data : personalDataList) {
            if (data.getUserId() == user.getUserId()) {
                return data;
            }
        }
        return UserPersonalData.defaultOf(user);
    }

    @GetMapping("/user/{userId}")
    public HttpResponseEntity<UserDetailsVo> getUserDetails(
            @PathVariable Long userId) {
        User user = userManageService.getUser(userId);
        UserPersonalData userPersonalData =
                userPersonalDataService.getPersonalData(user);
        UserDetailsVo userDetailsVo =
                UserDetailsVo.of(user, userPersonalData);
        return HttpResponseEntity.success(userDetailsVo);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable String userId) {

    }

    @PutMapping("/user/{userId}")
    public void updateUser(@PathVariable String userId) {

    }

    @PostMapping("/user")
    public void createUser() {

    }

    @PutMapping("/user/{userId}/blocks")
    public void blockUser(@PathVariable String userId) {

    }

    @DeleteMapping("/user/{userId}/blocks")
    public void unblockUser(@PathVariable String userId) {

    }

    @GetMapping("/user/{userId}/blocks")
    public void getBlockedUserList(@PathVariable String userId) {

    }
}
