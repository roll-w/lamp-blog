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

package space.lingu.lamp.web.domain.userdetails;

import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

import java.io.Serializable;

/**
 * User personal data.
 *
 * @author RollW
 */
@DataTable(tableName = "user_personal_data")
@SuppressWarnings({"unused", "ClassCanBeRecord"})
public final class UserPersonalData implements Serializable {
    @PrimaryKey
    @DataColumn(name = "id")
    private final long userId;

    @DataColumn(name = "nickname")
    private final String nickname;

    @DataColumn(name = "avatar")
    private final String avatar;

    @DataColumn(name = "birthday")
    private final Birthday birthday;

    @DataColumn(name = "introduction")
    private final String introduction;

    @DataColumn(name = "gender")
    private final Gender gender;

    @DataColumn(name = "location")
    private final String location;

    @DataColumn(name = "website")
    private final String website;

    public UserPersonalData(long userId, String nickname, String avatar,
                            Birthday birthday, String introduction,
                            Gender gender, String location, String website) {
        this.userId = userId;
        this.nickname = nickname;
        this.avatar = avatar;
        this.birthday = birthday;
        this.introduction = introduction;
        this.gender = gender;
        this.location = location;
        this.website = website;
    }

    public long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public Birthday getBirthday() {
        return birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public Gender getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static UserPersonalData defaultOf(UserIdentity user) {
        return new Builder()
                .setUserId(user.getUserId())
                .setBirthday(null)
                .setIntroduction(null)
                .setGender(Gender.PRIVATE)
                .setNickname(user.getUsername())
                .build();
    }

    public static final class Builder {
        @DataColumn(name = "id")
        private long userId;

        @DataColumn(name = "nickname")
        private String nickname;

        @DataColumn(name = "avatar")
        private String avatar;

        @DataColumn(name = "birthday")
        private Birthday birthday;

        @DataColumn(name = "introduction")
        private String introduction;

        @DataColumn(name = "gender")
        private Gender gender;

        @DataColumn(name = "location")
        private String location;

        @DataColumn(name = "website")
        private String website;

        public Builder() {
        }

        public Builder(UserPersonalData userPersonalData) {
            this.userId = userPersonalData.userId;
            this.nickname = userPersonalData.nickname;
            this.avatar = userPersonalData.avatar;
            this.gender = userPersonalData.gender;
            this.birthday = userPersonalData.birthday;
            this.introduction = userPersonalData.introduction;
            this.location = userPersonalData.location;
            this.website = userPersonalData.website;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        public Builder setAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        public Builder setBirthday(Birthday birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder setIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public Builder setGender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Builder setLocation(String location) {
            this.location = location;
            return this;
        }

        public Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public long getUserId() {
            return userId;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public Birthday getBirthday() {
            return birthday;
        }

        public String getIntroduction() {
            return introduction;
        }

        public Gender getGender() {
            return gender;
        }

        public String getLocation() {
            return location;
        }

        public String getWebsite() {
            return website;
        }

        public UserPersonalData build() {
            return new UserPersonalData(userId, nickname, avatar,
                    birthday, introduction, gender, location, website);
        }
    }
}
