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
import space.lingu.lamp.web.domain.user.AttributedUser;
import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.UserIdentity;
import space.lingu.lamp.web.domain.user.common.UserViewException;
import space.lingu.lamp.web.domain.user.event.OnUserCreateEvent;
import space.lingu.lamp.web.domain.user.filter.UserFilteringInfo;
import space.lingu.lamp.web.domain.user.filter.UserFilteringInfoType;
import space.lingu.lamp.web.domain.user.filter.UserInfoFilter;
import space.lingu.lamp.web.domain.user.repository.UserRepository;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.page.Offset;

import java.util.Collections;
import java.util.List;

/**
 * @author RollW
 */
@Service
public class UserServiceImpl implements UserSignatureProvider,
        UserManageService, UserSearchService {
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
    public List<AttributedUser> getUsers(int page, int size) {
        return Collections.unmodifiableList(
                userRepository.get(Offset.of(page, size))
        );
    }

    @Override
    public List<AttributedUser> getUsers() {
        return Collections.unmodifiableList(
                userRepository.get()
        );
    }

    @Override
    public UserIdentity findUser(long userId) throws UserViewException {
        User user = userRepository.getById(userId);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        if (!user.isEnabled()) {
            throw new UserViewException(UserErrorCode.ERROR_USER_DISABLED);
        }
        if (user.isCanceled()) {
            throw new UserViewException(UserErrorCode.ERROR_USER_CANCELED);
        }
        return user;
    }

    @Override
    public UserIdentity findUser(UserIdentity userIdentity) throws UserViewException {
        if (userIdentity == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        return findUser(userIdentity.getUserId());
    }

    @Override
    public List<? extends UserIdentity> findUsers() {
        // TODO: filter canceled user
        return userRepository.get();
    }

    @Override
    public List<? extends UserIdentity> findUsers(List<Long> ids) {
        return userRepository.getByIds(ids);
    }
}
