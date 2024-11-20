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
package space.lingu.lamp.security.authentication.registration.service

import org.slf4j.info
import org.slf4j.logger
import org.springframework.stereotype.Service
import space.lingu.lamp.security.authentication.registration.Registration
import space.lingu.lamp.security.authentication.registration.RegistrationInterceptor
import space.lingu.lamp.security.authentication.registration.SimpleRegistration
import space.lingu.lamp.user.Role
import space.lingu.lamp.user.repository.UserRepository

/**
 * Interceptor that promotes the first user to admin.
 *
 * @author RollW
 */
@Service
class FirstUserRegistrationInterceptor(
    private val userRepository: UserRepository
) : RegistrationInterceptor {
    private var hasUsers = false

    override fun preRegistration(registration: Registration): Registration {
        if (hasUsers) {
            return registration
        }
        hasUsers = userRepository.hasUsers()
        if (hasUsers) {
            return registration
        }
        logger.info {
            "First user '${registration.username}' registration detected, promoting user to admin."
        }
        return SimpleRegistration(
            registration.username,
            registration.password,
            registration.email,
            Role.ADMIN,
            true
        )
    }

    companion object {
        private val logger = logger<FirstUserRegistrationInterceptor>()
    }
}
