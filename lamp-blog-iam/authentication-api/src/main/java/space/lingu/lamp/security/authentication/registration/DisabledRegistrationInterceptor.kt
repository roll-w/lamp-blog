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

package space.lingu.lamp.security.authentication.registration

/**
 * @author RollW
 */
class DisabledRegistrationInterceptor : RegistrationInterceptor {
    var enabled = false

    override fun preRegistration(registration: Registration): Registration {
        if (enabled) {
            return registration
        }
        throw DisabledRegistrationException("Registration is disabled.")
    }
}