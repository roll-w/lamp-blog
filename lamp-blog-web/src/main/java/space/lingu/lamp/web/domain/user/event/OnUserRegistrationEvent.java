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

package space.lingu.lamp.web.domain.user.event;

import org.springframework.context.ApplicationEvent;
import space.lingu.lamp.web.domain.user.dto.UserInfo;

import java.util.Locale;

/**
 * @author RollW
 */
public class OnUserRegistrationEvent extends ApplicationEvent {
    private final UserInfo userInfo;
    /**
     * Confirm url prefix.
     */

    private final String url;
    private final Locale locale;

    public OnUserRegistrationEvent(UserInfo userInfo,
                                   Locale locale,
                                   String url) {
        super(userInfo);
        this.userInfo = userInfo;
        this.locale = locale;
        this.url = url;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public String getUrl() {
        return url;
    }

    public Locale getLocale() {
        return locale;
    }
}
