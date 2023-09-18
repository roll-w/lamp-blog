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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.controller.Api;
import space.lingu.lamp.web.controller.user.vo.UserCommonDetailsVo;
import space.lingu.lamp.web.domain.storage.StorageUrlProvider;
import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.lamp.web.domain.user.common.UserViewException;
import space.lingu.lamp.web.domain.user.service.UserSearchService;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.lamp.web.domain.userdetails.UserPersonalDataService;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.system.ContextThreadAware;

/**
 * @author RollW
 */
@Api
public class UserController {
    private final ContextThreadAware<ApiContext> apiContextThreadAware;
    private final UserSearchService userSearchService;
    private final UserPersonalDataService userPersonalDataService;
    private final StorageUrlProvider storageUrlProvider;

    public UserController(ContextThreadAware<ApiContext> apiContextThreadAware,
                          UserSearchService userSearchService,
                          UserPersonalDataService userPersonalDataService,
                          StorageUrlProvider storageUrlProvider) {
        this.apiContextThreadAware = apiContextThreadAware;
        this.userSearchService = userSearchService;
        this.userPersonalDataService = userPersonalDataService;
        this.storageUrlProvider = storageUrlProvider;
    }

    @GetMapping("/user")
    public HttpResponseEntity<UserCommonDetailsVo> getAuthenticatedUser() {
        ApiContext context = apiContextThreadAware
                .getContextThread().getContext();
        UserIdentity userInfo = context.getUser();
        if (userInfo == null) {
            throw new UserViewException(AuthErrorCode.ERROR_UNAUTHORIZED_USE);
        }
        UserPersonalData userPersonalData =
                userPersonalDataService.getPersonalData(userInfo);
        return HttpResponseEntity.success(UserCommonDetailsVo.of(
                userInfo, userPersonalData,
                storageUrlProvider.getUrlOfStorage(
                        userPersonalData.getAvatar()),
                storageUrlProvider.getUrlOfStorage(
                        userPersonalData.getCover())
        ));
    }

    @GetMapping("/user/{userId}")
    public HttpResponseEntity<UserCommonDetailsVo> getUserInfo(
            @PathVariable("userId") Long userId) {
        UserIdentity userIdentity = userSearchService.findUser(userId);
        UserPersonalData userPersonalData =
                userPersonalDataService.getPersonalData(userIdentity);
        return HttpResponseEntity.success(UserCommonDetailsVo.of(
                userIdentity, userPersonalData,
                storageUrlProvider.getUrlOfStorage(
                        userPersonalData.getAvatar()),
                storageUrlProvider.getUrlOfStorage(
                        userPersonalData.getCover())
        ));
    }

    @PutMapping("/user/{userId}/blocks")
    public void blockUser(@PathVariable("userId") Long userId) {

    }

    @DeleteMapping("/user/{userId}/blocks")
    public void unblockUser(@PathVariable("userId") Long userId) {

    }
}
