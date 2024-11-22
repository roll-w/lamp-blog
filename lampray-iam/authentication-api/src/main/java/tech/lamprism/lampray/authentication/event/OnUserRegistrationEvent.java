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

package tech.lamprism.lampray.authentication.event;

import org.springframework.context.ApplicationEvent;
import tech.lamprism.lampray.user.AttributedUser;

import java.util.Locale;

/**
 * @author RollW
 */
public class OnUserRegistrationEvent extends ApplicationEvent {
    private final AttributedUser user;
    private final Locale locale;

    public OnUserRegistrationEvent(AttributedUser user,
                                   Locale locale) {
        super(user);
        this.user = user;
        this.locale = locale;
    }

    public AttributedUser getUser() {
        return user;
    }

    public Locale getLocale() {
        return locale;
    }
}
