/*
 * Copyright (C) 2022 Lingu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.lingu.lamp.web.event.user;

import org.springframework.context.ApplicationEvent;
import space.lingu.lamp.web.data.dto.user.UserInfo;

import java.util.Locale;

/**
 * @author RollW
 */
public class OnUserRegistrationEvent extends ApplicationEvent {
    /**
     * Confirm url prefix.
     */
    private final String url;
    private final Locale locale;
    private final UserInfo user;

    public OnUserRegistrationEvent(UserInfo user,
                                   Locale locale,
                                   String url) {
        super(user);
        this.user = user;
        this.locale = locale;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public Locale getLocale() {
        return locale;
    }

    public UserInfo getUser() {
        return user;
    }
}
