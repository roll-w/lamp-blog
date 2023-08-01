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

package space.lingu.lamp.web.common;

import org.springframework.http.HttpMethod;
import space.lingu.NonNull;
import space.lingu.Nullable;
import space.lingu.lamp.web.domain.user.UserIdentity;
import tech.rollw.common.web.system.SystemContext;

import java.util.Locale;

/**
 * @author RollW
 */
public class ApiContext implements SystemContext {
    private final boolean admin;
    private final String ip;
    private final Locale locale;
    private final HttpMethod method;
    private final UserIdentity user;

    public ApiContext(boolean admin, String ip,
                      Locale locale, HttpMethod method,
                      UserIdentity user) {
        this.admin = admin;
        this.ip = ip;
        this.locale = locale;
        this.method = method;
        this.user = user;
    }

    @Override
    public Object getObject(@NonNull String key) {
        return switch (key) {
            case "admin" -> admin;
            case "ip" -> ip;
            case "locale" -> locale;
            case "method" -> method;
            case "user" -> user;
            default -> null;
        };
    }

    public boolean hasUser() {
        return user != null;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getIp() {
        return ip;
    }

    public Locale getLocale() {
        return locale;
    }

    public HttpMethod getMethod() {
        return method;
    }

    @Nullable
    public UserIdentity getUser() {
        return user;
    }

    public ApiContext fork(UserIdentity user) {
        if (user == this.user) {
            return this;
        }
        return new ApiContext(admin, ip, locale, method, user);
    }
}
