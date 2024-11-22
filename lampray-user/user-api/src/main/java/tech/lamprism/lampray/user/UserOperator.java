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

package tech.lamprism.lampray.user;


import space.lingu.NonNull;
import tech.rollw.common.web.CommonRuntimeException;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceKind;
import tech.rollw.common.web.system.SystemResourceOperator;

/**
 * @author RollW
 */
public interface UserOperator extends SystemResourceOperator<Long>,
        SystemResource<Long>, AttributedUser {
    @Override
    void setCheckDeleted(boolean checkDeleted);

    @Override
    boolean isCheckDeleted();

    @Override
    Long getResourceId();

    @NonNull
    @Override
    default SystemResourceKind getSystemResourceKind() {
        return UserResourceKind.INSTANCE;
    }

    @Override
    UserOperator disableAutoUpdate();

    @Override
    UserOperator enableAutoUpdate();

    @Override
    boolean isAutoUpdateEnabled();

    @Override
    UserOperator update() throws CommonRuntimeException;

    @Override
    UserOperator delete() throws CommonRuntimeException;

    @Override
    UserOperator rename(String newName) throws CommonRuntimeException;

    @Override
    UserOperator getSystemResource();

    UserOperator setEmail(String email)
            throws CommonRuntimeException;

    UserOperator setRole(Role role)
            throws CommonRuntimeException;

    UserOperator setPassword(String password)
            throws CommonRuntimeException;

    UserOperator setPassword(String oldPassword, String password)
            throws CommonRuntimeException;

    UserOperator setEnabled(boolean enabled)
            throws CommonRuntimeException;

    UserOperator setLocked(boolean locked)
            throws CommonRuntimeException;

    UserOperator setCanceled(boolean canceled)
            throws CommonRuntimeException;
}
