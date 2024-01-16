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

package space.lingu.lamp.web.domain.content;

import space.lingu.NonNull;
import tech.rollw.common.web.BusinessRuntimeException;
import tech.rollw.common.web.system.SystemResourceKind;
import tech.rollw.common.web.system.SystemResourceOperator;

/**
 * @author RollW
 */
public interface ContentOperator extends SystemResourceOperator<Long>, ContentDetails {
    @Override
    ContentOperator update() throws BusinessRuntimeException;

    @Override
    ContentOperator delete() throws BusinessRuntimeException;

    ContentOperator forbidden() throws BusinessRuntimeException;

    @Override
    ContentOperator restore() throws BusinessRuntimeException;

    @Override
    ContentOperator rename(String newName) throws BusinessRuntimeException;

    ContentOperator setContent(String content) throws BusinessRuntimeException;

    ContentOperator setPassword(String password) throws BusinessRuntimeException;

    ContentOperator setMetadata(ContentDetailsMetadata metadata) throws BusinessRuntimeException;

    @Override
    ContentOperator disableAutoUpdate();

    @Override
    ContentOperator enableAutoUpdate();

    @Override
    boolean isAutoUpdateEnabled();

    @Override
    void setCheckDeleted(boolean checkDeleted);

    @Override
    boolean isCheckDeleted();

    @Override
    default ContentOperator getSystemResource() {
        return this;
    }

    @Override
    default Long getResourceId() {
        return getContentId();
    }

    @NonNull
    @Override
    default SystemResourceKind getSystemResourceKind() {
        return getContentType().getSystemResourceKind();
    }
}
