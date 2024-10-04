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

package space.lingu.lamp.user.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.user.AttributedUser;
import space.lingu.lamp.user.AttributedUserDetails;
import space.lingu.lamp.user.Role;
import space.lingu.lamp.user.User;
import space.lingu.lamp.user.UserManageService;
import space.lingu.lamp.user.UserProvider;
import space.lingu.lamp.user.UserSearchService;
import space.lingu.lamp.user.UserSignatureProvider;
import space.lingu.lamp.user.UserTrait;
import space.lingu.lamp.user.UserViewException;
import space.lingu.lamp.user.event.OnUserCreateEvent;
import space.lingu.lamp.user.filter.UserFilteringInfo;
import space.lingu.lamp.user.filter.UserFilteringInfoType;
import space.lingu.lamp.user.filter.UserInfoFilter;
import space.lingu.lamp.user.repository.UserDo;
import space.lingu.lamp.user.repository.UserRepository;
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
        UserDo user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        return "LampBlogUserSignature-" + user.getPassword();
    }

    @Override
    public AttributedUser createUser(String username, String password,
                                     String email, Role role, boolean enable) {
        if (userRepository.isExistByUsername(username)) {
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
                .setRegisterTime(time)
                .setUpdateTime(time)
                .setEmail(email)
                .build();
        UserDo inserted = userRepository.save(UserDo.toDo(user));
        OnUserCreateEvent onUserCreateEvent = new OnUserCreateEvent(inserted);
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
    public AttributedUserDetails getUser(long userId) throws UserViewException {
        UserDo user = userRepository.getByUserId(userId).orElse(null);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        return user.toUser();
    }

    @Override
    public AttributedUserDetails getUser(String username) throws UserViewException {
        UserDo user = userRepository.getByUsername(username).orElse(null);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        return user.toUser();
    }

    @Override
    public AttributedUserDetails getUser(UserTrait userTrait)
            throws UserViewException {
        return getUser(userTrait.getUserId());
    }

    @Override
    public List<AttributedUserDetails> getUsers() {
        return Collections.unmodifiableList(
                userRepository.findAll()
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
