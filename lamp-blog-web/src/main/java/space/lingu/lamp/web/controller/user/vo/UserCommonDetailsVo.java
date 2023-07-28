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

package space.lingu.lamp.web.controller.user.vo;

import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.lamp.web.domain.userdetails.Birthday;
import space.lingu.lamp.web.domain.userdetails.Gender;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;

/**
 * When the user is logged in, the user's personal information is displayed on the page.
 *
 * @author RollW
 */
public record UserCommonDetailsVo(
        long userId,
        Role role,
        String username,
        String email,
        String nickname,
        String avatar,
        String introduction,
        Gender gender,
        Birthday birthday,
        String website,
        String location
) {
    // TODO: add followers/following count

    public static UserCommonDetailsVo of(UserIdentity userIdentity,
                                         UserPersonalData data) {
        return new UserCommonDetailsVo(
                userIdentity.getUserId(),
                userIdentity.getRole(),
                userIdentity.getUsername(),
                userIdentity.getEmail(),
                data.getNickname(),
                data.getAvatar(),
                data.getIntroduction(),
                data.getGender(),
                data.getBirthday(),
                data.getWebsite(),
                data.getLocation()
        );
    }
}
