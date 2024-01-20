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

package space.lingu.lamp.web.domain.userdetails.service;

import org.springframework.stereotype.Service;
import space.lingu.lamp.web.domain.user.AttributedUser;
import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.lamp.web.domain.user.UserProvider;
import space.lingu.lamp.web.domain.userdetails.Birthday;
import space.lingu.lamp.web.domain.userdetails.Gender;
import space.lingu.lamp.web.domain.userdetails.UserDataField;
import space.lingu.lamp.web.domain.userdetails.UserDataFieldType;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.lamp.web.domain.userdetails.UserPersonalDataService;
import space.lingu.lamp.web.domain.userdetails.repository.UserPersonalDataRepository;

import java.util.List;

/**
 * @author RollW
 */
@Service
public class UserPersonalDataServiceImpl implements UserPersonalDataService {
    private final UserProvider userProvider;
    private final UserPersonalDataRepository userPersonalDataRepository;

    public UserPersonalDataServiceImpl(UserProvider userProvider,
                                       UserPersonalDataRepository userPersonalDataRepository) {
        this.userProvider = userProvider;
        this.userPersonalDataRepository = userPersonalDataRepository;
    }

    @Override
    public UserPersonalData getPersonalData(long userId) {
        UserPersonalData data = userPersonalDataRepository.getById(userId);
        if (data == null) {
            AttributedUser user = userProvider.getUser(userId);
            return UserPersonalData.defaultOf(user);
        }
        if (UserPersonalData.checkNecessaryFields(data)) {
            return data;
        }
        AttributedUser user = userProvider.getUser(userId);
        return UserPersonalData.replaceWithDefault(user, data);
    }

    @Override
    public UserPersonalData getPersonalData(UserIdentity userIdentity) {
        UserPersonalData data = userPersonalDataRepository.getById(userIdentity.getUserId());
        if (data == null) {
            return UserPersonalData.defaultOf(userIdentity);
        }
        if (UserPersonalData.checkNecessaryFields(data)) {
            return data;
        }
        return UserPersonalData.replaceWithDefault(userIdentity, data);
    }

    @Override
    public List<UserPersonalData> getPersonalData(List<? extends UserIdentity> userIdentities) {
        List<Long> ids = userIdentities.stream().map(UserIdentity::getUserId)
                .toList();
        List<UserPersonalData> userPersonalData = getPersonalDataByIds(ids);
        return userPersonalData.stream().map(data -> {
            if (UserPersonalData.checkNecessaryFields(data)) {
                return data;
            }
            return UserPersonalData.replaceWithDefault(
                    userIdentities.stream()
                            .filter(identity -> identity.getUserId() == data.getUserId())
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Get personal data error")),
                    data
            );
        }).toList();
    }

    @Override
    public List<UserPersonalData> getPersonalDataByIds(List<Long> ids) {
        return userPersonalDataRepository.getByIds(ids);
    }

    @Override
    public void updatePersonalData(long userId,
                                   UserDataFieldType type,
                                   Object value) {
        updatePersonalData(userId, new UserDataField(type, value));
    }

    @Override
    public void updatePersonalData(long userId, UserDataField... fields) {
        if (fields.length == 0) {
            return;
        }
        UserPersonalData userPersonalData = getPersonalData(userId);
        UserPersonalData.Builder builder = toBuilder(userPersonalData);

        for (UserDataField field : fields) {
            setBuilderValue(builder, field);
        }
        if (userPersonalData == null) {
            userPersonalDataRepository.insert(builder.build());
            return;
        }
        userPersonalDataRepository.update(builder.build());
    }

    private void setBuilderValue(UserPersonalData.Builder builder,
                                 UserDataField field) {
        switch (field.type()) {
            case AVATAR -> builder.setAvatar((String) field.value());
            case GENDER -> builder.setGender(Gender.of(field.value()));
            case INTRO -> builder.setIntroduction((String) field.value());
            case WEBSITE -> builder.setWebsite((String) field.value());
            case LOCATION -> builder.setLocation((String) field.value());
            case BIRTHDAY -> builder.setBirthday(Birthday.fromString(
                    (String) field.value())
            );
            case NICKNAME -> builder.setNickname((String) field.value());
        }
    }

    private UserPersonalData.Builder toBuilder(UserPersonalData data) {
        if (data != null) {
            return data.toBuilder();
        }
        return UserPersonalData.builder();
    }

    @Override
    public void createPersonalData(UserPersonalData data) {
        if (data == null) {
            return;
        }
        userPersonalDataRepository.insert(data);
    }
}
