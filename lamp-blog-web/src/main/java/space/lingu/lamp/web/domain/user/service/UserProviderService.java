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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.lamp.web.domain.user.AttributedUser;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.common.UserViewException;
import space.lingu.lamp.web.domain.user.filter.UserInfoFilter;
import space.lingu.lamp.web.domain.user.repository.UserRepository;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import space.lingu.lamp.web.domain.userdetails.repository.UserPersonalDataRepository;
import tech.rollw.common.web.BusinessRuntimeException;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceKind;
import tech.rollw.common.web.system.SystemResourceOperator;
import tech.rollw.common.web.system.SystemResourceOperatorFactory;
import tech.rollw.common.web.system.SystemResourceProvider;
import tech.rollw.common.web.system.UnsupportedKindException;

/**
 * @author RollW
 */
@Service
public class UserProviderService implements SystemResourceProvider<Long>,
        SystemResourceOperatorFactory<Long>, UserOperatorDelegate {
    private final UserRepository userRepository;
    private final UserPersonalDataRepository userPersonalDataRepository;
    private final UserInfoFilter userInfoFilter;
    private final PasswordEncoder passwordEncoder;

    public UserProviderService(UserRepository userRepository,
                               UserPersonalDataRepository userPersonalDataRepository,
                               UserInfoFilter userInfoFilter,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userPersonalDataRepository = userPersonalDataRepository;
        this.userInfoFilter = userInfoFilter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean supports(@NonNull SystemResourceKind systemResourceKind) {
        return systemResourceKind == LampSystemResourceKind.USER;
    }

    @NonNull
    @Override
    public SystemResourceOperator<Long> createResourceOperator(
            SystemResource<Long> systemResource, boolean checkDelete) {
        User user = getUser(systemResource.getResourceId());
        return new UserOperatorImpl(user, this, checkDelete);
    }

    @NonNull
    @Override
    public AttributedUser provide(@NonNull SystemResource<Long> rawSystemResource)
            throws BusinessRuntimeException, UnsupportedKindException {
        if (rawSystemResource instanceof AttributedUser attributedUser) {
            return attributedUser;
        }
        return getUser(rawSystemResource.getResourceId());
    }

    private User getUser(long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        return user;
    }

    @Override
    public UserPersonalData getUserPersonalData(Long id) {
        return userPersonalDataRepository.getById(id);
    }

    @Override
    public void updateUser(User user) {
        userRepository.update(user);
    }

    @Override
    public void updateUserPersonalData(UserPersonalData userPersonalData) {
        userPersonalDataRepository.update(userPersonalData);
    }

    @Override
    public boolean checkUsernameExist(String username, long id) {
        User user = userRepository.getByUsername(username);
        if (user == null) {
            return false;
        }
        if (user.isCanceled()) {
            return false;
        }
        return user.getUserId() != id;
    }

    @Override
    public boolean checkEmailExist(String email, long id) {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            return false;
        }
        if (user.isCanceled()) {
            return false;
        }
        return user.getUserId() != id;
    }

    @Override
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    public UserInfoFilter getUserInfoFilter() {
        return userInfoFilter;
    }

}
