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

package tech.lamprism.lampray.push.mail;

import com.google.common.base.Strings;
import tech.lamprism.lampray.push.PushType;
import tech.lamprism.lampray.push.PushUser;

/**
 * @author RollW
 */
public class MailPushUser implements PushUser {
    private final String email;
    private final String name;

    public MailPushUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public Long getId() {
        return SYSTEM;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public PushType getPushType() {
        return PushType.EMAIL;
    }

    public String toSenderName() {
        if (Strings.isNullOrEmpty(name)) {
            return email;
        }

        return name + "<" + email + ">";
    }
}
