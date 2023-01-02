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

package space.lingu.lamp.web.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import space.lingu.lamp.MessagePackage;
import space.lingu.lamp.web.data.database.repository.UserRepository;
import space.lingu.lamp.web.data.dto.user.UserInfo;
import space.lingu.lamp.web.data.entity.user.Role;
import space.lingu.lamp.web.data.entity.user.User;
import space.lingu.lamp.web.event.user.OnUserRegistrationEvent;

import java.util.Locale;

/**
 * @author RollW
 */
@Service
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(ApplicationEventPublisher eventPublisher,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MessagePackage<UserInfo> registerUser(String username, String password,
                                                 String email, Role role) {
        Long userId = userRepository.getUserIdByName(username);
        if (!User.isInvalidId(userId)) {
            return MessagePackage.create(UserErrorCode.ERROR_USER_EXISTED,
                    "A user with same name is existed.", null
            );
        }
        if (userRepository.getUserByEmail(email) != null) {
            return MessagePackage.create(UserErrorCode.ERROR_EMAIL_EXISTED,
                    "A user with same email is existed.", null
            );
        }
        long registerTime = System.currentTimeMillis();
        String encodedPassword = passwordEncoder.encode(password);
        // TODO: check params
        User.Builder builder = User.builder()
                .setUsername(username)
                .setEmail(email)
                .setRole(role)
                .setPassword(encodedPassword)
                .setRegisterTime(registerTime)
                .setLocked(false)
                .setEnabled(false)
                .setAccountExpired(false);
        if (!userRepository.hasUsers()) {
            builder.setEnabled(true)
                    .setRole(Role.ADMIN);
        }
        User user = builder.build();
        if (!user.isEnabled()) {
            OnUserRegistrationEvent event = new OnUserRegistrationEvent(
                    UserInfo.from(user), Locale.getDefault(), "");
            eventPublisher.publishEvent(event);
        }
        long id = userRepository.insertUser(user);
        logger.info("Register username: {}, email: {}, role: {}, id: {}",
                username, email, role, id);
        return MessagePackage.success(UserInfo.from(user));
    }
}
