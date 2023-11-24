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

import org.springframework.stereotype.Service;
import space.lingu.NonNull;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.lamp.web.domain.user.AttributedUser;
import space.lingu.lamp.web.domain.user.User;
import space.lingu.lamp.web.domain.user.common.UserViewException;
import space.lingu.lamp.web.domain.user.repository.UserRepository;
import tech.rollw.common.web.BusinessRuntimeException;
import tech.rollw.common.web.UserErrorCode;
import tech.rollw.common.web.system.SystemResource;
import tech.rollw.common.web.system.SystemResourceKind;
import tech.rollw.common.web.system.SystemResourceProvider;
import tech.rollw.common.web.system.UnsupportedKindException;

/**
 * @author RollW
 */
@Service
public class UserProviderService implements SystemResourceProvider<Long> {
    private final UserRepository userRepository;

    public UserProviderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(@NonNull SystemResourceKind systemResourceKind) {
        return systemResourceKind == LampSystemResourceKind.USER;
    }

    @NonNull
    @Override
    public AttributedUser provide(@NonNull SystemResource<Long> rawSystemResource)
            throws BusinessRuntimeException, UnsupportedKindException {
        if (rawSystemResource instanceof AttributedUser attributedUser) {
            return attributedUser;
        }
        User user = userRepository.getById(rawSystemResource.getResourceId());
        if (user == null) {
            throw new UserViewException(UserErrorCode.ERROR_USER_NOT_EXIST);
        }
        return user;
    }
}
