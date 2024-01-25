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
import space.lingu.lamp.web.common.ParameterMissingException;
import space.lingu.lamp.web.domain.user.Role;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.UserOperator;
import space.lingu.lamp.web.domain.user.common.UserViewException;
import space.lingu.lamp.web.domain.user.filter.UserFilteringInfo;
import space.lingu.lamp.web.domain.user.filter.UserFilteringInfoType;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;
import tech.rollw.common.web.BusinessRuntimeException;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.UserErrorCode;

/**
 * @author RollW
 */
public class UserOperatorImpl implements UserOperator {
    private boolean checkDelete;
    private boolean autoUpdateEnabled;

    private User user;
    private final User.Builder userBuilder;
    private final UserOperatorDelegate delegate;

    private UpdateFlag updateFlag = UpdateFlag.NONE;

    private UserPersonalData userPersonalData;
    private UserPersonalData.Builder userPersonalDataBuilder;

    public UserOperatorImpl(User user, UserOperatorDelegate delegate,
                            boolean checkDelete) {
        this.user = user;
        this.userBuilder = user.toBuilder();
        this.delegate = delegate;
        this.checkDelete = checkDelete;
    }

    @Override
    public void setCheckDeleted(boolean checkDeleted) {
        this.checkDelete = checkDeleted;
    }

    @Override
    public boolean isCheckDeleted() {
        return checkDelete;
    }

    @Override
    public Long getResourceId() {
        return getUserId();
    }

    @Override
    public UserOperator disableAutoUpdate() {
        this.autoUpdateEnabled = false;
        return this;
    }

    @Override
    public UserOperator enableAutoUpdate() {
        this.autoUpdateEnabled = true;
        return this;
    }

    @Override
    public boolean isAutoUpdateEnabled() {
        return autoUpdateEnabled;
    }

    @Override
    public UserOperator update() throws BusinessRuntimeException {
        if (autoUpdateEnabled) {
            return this;
        }
        if (!updateFlag.isUpdate()) {
            return this;
        }
        if (updateFlag.isUpdateUser()) {
            user = userBuilder
                    .setUpdateTime(System.currentTimeMillis())
                    .build();
            delegate.updateUser(user);
        }
        if (updateFlag.isUpdatePersonalData()) {
            userPersonalData = userPersonalDataBuilder
                    .setUpdateTime(System.currentTimeMillis())
                    .build();
            delegate.updateUserPersonalData(userPersonalData);
        }
        updateFlag = UpdateFlag.NONE;
        return this;
    }

    @Override
    public UserOperator delete() throws BusinessRuntimeException {
        checkDelete();
        userBuilder.setCanceled(true);
        return updateInternal();
    }

    @Override
    public UserOperator rename(String username) throws BusinessRuntimeException {
        checkDelete();
        if (username == null) {
            throw new ParameterMissingException("username");
        }
        if (user.getUsername().equals(username)) {
            return this;
        }
        checkRule(username, UserFilteringInfoType.USERNAME);

        if (delegate.checkUsernameExist(username, user.getUserId())) {
            throw new UserViewException(UserErrorCode.ERROR_USER_EXISTED);
        }

        userBuilder.setUsername(username);
        return updateInternal();
    }

    @Override
    public UserOperator setNickname(String nickname) throws BusinessRuntimeException {
        checkDelete();
        loadUserPersonalData();

        if (nickname == null) {
            throw new ParameterMissingException("nickname");
        }
        if (userPersonalData.getNickname().equals(nickname)) {
            return this;
        }
        checkRule(nickname, UserFilteringInfoType.NICKNAME);
        return updatePersonalDataInternal();
    }

    private UserOperator updatePersonalDataInternal() {
        if (!autoUpdateEnabled) {
            updateFlag =
                    updateFlag.plus(UpdateFlag.PERSONAL_DATA);
            return this;
        }
        userPersonalData = userPersonalDataBuilder
                .setUpdateTime(System.currentTimeMillis())
                .build();
        delegate.updateUserPersonalData(userPersonalData);
        return this;
    }

    @Override
    public UserOperator setEmail(String email)
            throws BusinessRuntimeException {
        checkDelete();
        if (email == null) {
            throw new ParameterMissingException("email");
        }
        if (user.getEmail().equals(email)) {
            return this;
        }

        checkRule(email, UserFilteringInfoType.EMAIL);
        if (delegate.checkEmailExist(email, user.getUserId())) {
            throw new UserViewException(UserErrorCode.ERROR_EMAIL_EXISTED);
        }

        userBuilder.setEmail(email);
        return updateInternal();
    }

    @Override
    public UserOperator setRole(Role role) throws BusinessRuntimeException {
        checkDelete();
        if (role == null) {
            throw new ParameterMissingException("role");
        }
        if (user.getRole().equals(role)) {
            return this;
        }

        userBuilder.setRole(role);
        return updateInternal();
    }

    @Override
    public UserOperator setPassword(String password) throws BusinessRuntimeException {
        checkDelete();
        if (password == null) {
            throw new ParameterMissingException("password");
        }
        if (user.getPassword().equals(password)) {
            return this;
        }

        checkRule(password, UserFilteringInfoType.PASSWORD);
        PasswordEncoder encoder = delegate.getPasswordEncoder();
        userBuilder.setPassword(
                encoder.encode(password)
        );
        return updateInternal();
    }

    private void checkRule(String value, UserFilteringInfoType type) {
        UserFilteringInfo info =
                new UserFilteringInfo(value, type);
        ErrorCode errorCode =
                delegate.getUserInfoFilter().filter(info);
        if (errorCode.failed()) {
            throw new UserViewException(errorCode);
        }
    }

    @Override
    public UserOperator setPassword(String oldPassword,
                                    String password)
            throws BusinessRuntimeException {
        checkDelete();
        if (oldPassword == null) {
            throw new ParameterMissingException("oldPassword");
        }
        if (password == null) {
            throw new ParameterMissingException("password");
        }
        PasswordEncoder encoder = delegate.getPasswordEncoder();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new UserViewException(UserErrorCode.ERROR_PASSWORD_NOT_CORRECT);
        }
        checkRule(password, UserFilteringInfoType.PASSWORD);
        userBuilder.setPassword(encoder.encode(password));

        return updateInternal();
    }

    @Override
    public UserOperator setEnabled(boolean enabled) throws BusinessRuntimeException {
        checkDelete();
        if (user.isEnabled() == enabled) {
            return this;
        }
        userBuilder.setEnabled(enabled);
        return updateInternal();
    }

    @Override
    public UserOperator setLocked(boolean locked) throws BusinessRuntimeException {
        checkDelete();
        if (user.isLocked() == locked) {
            return this;
        }
        userBuilder.setLocked(locked);
        return updateInternal();
    }

    @Override
    public UserOperator setCanceled(boolean canceled) throws BusinessRuntimeException {
        checkDelete();
        if (user.isCanceled() == canceled) {
            return this;
        }
        userBuilder.setCanceled(canceled);
        return updateInternal();
    }

    @Override
    public UserOperator getSystemResource() {
        return this;
    }

    private void checkDelete() {
        if (!checkDelete) {
            return;
        }
        if (!user.isEnabled()) {
            throw new UserViewException(UserErrorCode.ERROR_USER_DISABLED);
        }
        if (user.isCanceled()) {
            throw new UserViewException(UserErrorCode.ERROR_USER_CANCELED);
        }
    }

    private UserOperator updateInternal() {
        if (!autoUpdateEnabled) {
            updateFlag = updateFlag.plus(UpdateFlag.USER);
            return this;
        }
        user = userBuilder
                .setUpdateTime(System.currentTimeMillis())
                .build();
        delegate.updateUser(user);
        updateFlag = UpdateFlag.NONE;
        return this;
    }

    private void loadUserPersonalData() {
        if (userPersonalData != null) {
            return;
        }

        UserPersonalData personalData =
                delegate.getUserPersonalData(user.getId());
        if (personalData != null) {
            this.userPersonalData = personalData;
            this.userPersonalDataBuilder = personalData.toBuilder();
            return;
        }
        this.userPersonalData = UserPersonalData.defaultOf(this);
        this.userPersonalDataBuilder = this.userPersonalData.toBuilder();
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public long getUserId() {
        return user.getUserId();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public Role getRole() {
        return user.getRole();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    @Override
    public boolean isLocked() {
        return user.isLocked();
    }

    @Override
    public boolean isCanceled() {
        return user.isCanceled();
    }

    @Override
    public long getCreateTime() {
        return user.getCreateTime();
    }

    @Override
    public long getUpdateTime() {
        return user.getUpdateTime();
    }

    private enum UpdateFlag {
        USER,
        PERSONAL_DATA,
        ALL,
        NONE;

        boolean isUpdate() {
            return this != NONE;
        }

        boolean isUpdateUser() {
            return this == USER || this == ALL;
        }

        boolean isUpdatePersonalData() {
            return this == PERSONAL_DATA || this == ALL;
        }

        public UpdateFlag plus(UpdateFlag updateFlag) {
            if (this == ALL) {
                return this;
            }
            if (this == NONE) {
                return updateFlag;
            }
            if (this == updateFlag) {
                return this;
            }
            return ALL;
        }
    }
}
