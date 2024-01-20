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

package space.lingu.lamp.web.domain.user.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.user.*;
import space.lingu.lamp.web.domain.user.common.UserViewException;
import space.lingu.lamp.web.domain.user.event.OnUserCreateEvent;
import space.lingu.lamp.web.domain.user.filter.UserFilteringInfo;
import space.lingu.lamp.web.domain.user.filter.UserFilteringInfoType;
import space.lingu.lamp.web.domain.user.filter.UserInfoFilter;
import space.lingu.lamp.web.domain.user.repository.UserRepository;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.UserErrorCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author RollW
 */
@Service
public class UserServiceImpl implements UserSignatureProvider,
        UserManageService, UserSearchService, UserProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserInfoFilter userInfoFilter;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           UserInfoFilter userInfoFilter,
                           ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userInfoFilter = userInfoFilter;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public String getSignature(long userId) {
        User user = userRepository.getById(userId);
        if (user == null) {
            return null;
        }
        return "LampBlogUserSignature-" + user.getPassword();
    }

    @Override
    public AttributedUser createUser(String username, String password,
                                     String email, Role role, boolean enable) {
        if (userRepository.isExistByEmail(username)) {
            throw new UserViewException(UserErrorCode.ERROR_USER_EXISTED);
        }
        if (userRepository.isExistByEmail(email)) {
            throw new UserViewException(UserErrorCode.ERROR_EMAIL_EXISTED);
        }
        validate(username, password, email);

        long time = System.currentTimeMillis();
        User user = User.builder()
                .setUsername(username)
                .setPassword(passwordEncoder.encode(password))
                .setRole(role)
                .setEnabled(enable)
                .setAccountExpired(false)
                .setRegisterTime(time)
                .setUpdateTime(time)
                .setEmail(email)
                .build();
        User inserted = userRepository.insert(user);
        OnUserCreateEvent onUserCreateEvent =
                new OnUserCreateEvent(inserted);
        eventPublisher.publishEvent(onUserCreateEvent);
        return inserted;
    }

    private void validate(String username,
                          String password,
                          String email) {
        List<UserFilteringInfo> filteringInfos = List.of(
                new UserFilteringInfo(username, UserFilteringInfoType.USERNAME),
                new UserFilteringInfo(password, UserFilteringInfoType.PASSWORD),
                new UserFilteringInfo(email, UserFilteringInfoType.EMAIL)
        );
        for (UserFilteringInfo filteringInfo : filteringInfos) {
            ErrorCode errorCode = userInfoFilter.filter(filteringInfo);
            if (errorCode.failed()) {
                throw new UserViewException(errorCode);
            }
        }
    }

    @Override
    public User getUser(long userId) throws UserViewException {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        return user;
    }

    @Override
    public AttributedUser getUser(UserIdentity userIdentity)
            throws UserViewException {
        return getUser(userIdentity.getUserId());
    }

    @Override
    public List<AttributedUser> getUsers() {
        return Collections.unmodifiableList(
                userRepository.get()
        );
    }

    @Override
    public List<AttributedUser> findUsers(@NonNull String keyword) {
        List<AttributedUser> res = new ArrayList<>();
        AttributedUser user = tryGetUserById(keyword);
        if (user != null) {
            res.add(user);
        }
        res.addAll(userRepository.searchBy(keyword));

        return res.stream()
                .distinct()
                .toList();
    }

    private AttributedUser tryGetUserById(String s) {
        try {
            return getUser(Long.parseLong(s));
        } catch (NumberFormatException | UserViewException e) {
            return null;
        }
    }

    @Override
    public List<AttributedUser> findUsers(List<Long> ids) {
        return Collections.unmodifiableList(
                userRepository.getByIds(ids)
        );
    }
}
