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

package space.lingu.lamp.security.authentication.registration;

import space.lingu.NonNull;

/**
 * Registration interceptor. Used to intercept the registration process.
 * <p>
 * The interceptor can be used to modify the registration data before it
 * is processed, can also be used to validate the registration data.
 * If the registration data is invalid, the interceptor can throw a
 * {@link RegistrationException}.
 *
 * @author RollW
 */
public interface RegistrationInterceptor {
    /**
     * Pre-registration interceptor.
     *
     * @param registration registration data.
     * @return registration data.
     * @throws RegistrationException if the registration data is invalid or the
     *                               registration process is failed.
     */
    @NonNull
    Registration preRegistration(@NonNull Registration registration)
            throws RegistrationException;
}
