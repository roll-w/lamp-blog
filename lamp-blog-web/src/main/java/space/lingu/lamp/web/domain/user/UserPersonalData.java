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

package space.lingu.lamp.web.domain.user;

import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;

import java.io.Serializable;
import java.util.Objects;

/**
 * 用户个人资料
 *
 * @author RollW
 */
@DataTable(tableName = "user_additional_info")
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
    private final long birthday;

    @DataColumn(name = "introduction")
    private final String introduction;

    @DataColumn(name = "gender")
    private final String gender;

    @DataColumn(name = "location")
    private final String location;

    @DataColumn(name = "website")
    private final String website;

    public UserPersonalData(long userId, String nickname, String avatar,
                            long birthday, String introduction,
                            String gender, String location, String website) {
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

    public long getBirthday() {
        return birthday;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getGender() {
        return gender;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public Builder toBuilder() {
        return new Builder()
                .setUserId(userId)
                .setNickname(nickname)
                .setAvatar(avatar)
                .setBirthday(birthday)
                .setIntroduction(introduction)
                .setWebsite(website)
                .setLocation(location);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPersonalData that = (UserPersonalData) o;
        return userId == that.userId && birthday == that.birthday &&
                Objects.equals(nickname, that.nickname) && Objects.equals(avatar, that.avatar) && Objects.equals(introduction, that.introduction) && Objects.equals(gender, that.gender) && Objects.equals(location, that.location) && Objects.equals(website, that.website);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, nickname, avatar, birthday, introduction, gender, location, website);
    }

    @Override
    public String toString() {
        return "UserPersonalData{" + "userId=" + userId +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday=" + birthday +
                ", introduction='" + introduction + '\'' +
                ", gender='" + gender + '\'' +
                ", location='" + location + '\'' +
                ", website='" + website + '\'' +
                '}';
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        @DataColumn(name = "id")
        private long userId;

        @DataColumn(name = "nickname")
        private String nickname;

        @DataColumn(name = "avatar")
        private String avatar;

        @DataColumn(name = "birthday")
        private long birthday;

        @DataColumn(name = "introduction")
        private String introduction;

        @DataColumn(name = "gender")
        private String gender;

        @DataColumn(name = "location")
        private String location;

        @DataColumn(name = "website")
        private String website;

        public Builder() {
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

        public Builder setBirthday(long birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder setIntroduction(String introduction) {
            this.introduction = introduction;
            return this;
        }

        public Builder setGender(String gender) {
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

        public long getBirthday() {
            return birthday;
        }

        public String getIntroduction() {
            return introduction;
        }

        public String getGender() {
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
