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

import com.google.common.base.Preconditions;
import space.lingu.Nullable;
import space.lingu.lamp.web.domain.user.dto.UserInfo;

import java.util.Locale;

/**
 * @author RollW
 */
public final class ApiContextHolder {
    private static final ThreadLocal<ApiContext> contextHolder = new ThreadLocal<>();

    public static void setContext(ApiContext context) {
        Preconditions.checkNotNull(context, "context cannot be null");
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }

    public static ApiContext getContext() {
        return contextHolder.get();
    }

    public static boolean hasContext() {
        return contextHolder.get() != null;
    }

    private ApiContextHolder() {
    }

    public record ApiContext(
            boolean admin,
            String ip,
            Locale locale,
            @Nullable
            UserInfo userInfo
    ) {
    }

}
