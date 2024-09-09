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

package space.lingu.lamp.user;


import space.lingu.NonNull;
import tech.rollw.common.web.BusinessRuntimeException;
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
    UserOperator update() throws BusinessRuntimeException;

    @Override
    UserOperator delete() throws BusinessRuntimeException;

    @Override
    UserOperator rename(String newName) throws BusinessRuntimeException;

    @Override
    UserOperator getSystemResource();

    UserOperator setEmail(String email)
            throws BusinessRuntimeException;

    UserOperator setRole(Role role)
            throws BusinessRuntimeException;

    UserOperator setPassword(String password)
            throws BusinessRuntimeException;

    UserOperator setPassword(String oldPassword, String password)
            throws BusinessRuntimeException;

    UserOperator setEnabled(boolean enabled)
            throws BusinessRuntimeException;

    UserOperator setLocked(boolean locked)
            throws BusinessRuntimeException;

    UserOperator setCanceled(boolean canceled)
            throws BusinessRuntimeException;
}
