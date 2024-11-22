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
import space.lingu.NonNull;
import tech.lamprism.lampray.RequestMetadata;
import tech.lamprism.lampray.user.UserIdentity;

/**
 * @author RollW
 */
public class OnUserLoginEvent extends ApplicationEvent {
    @NonNull
    private final UserIdentity userInfo;

    @NonNull
    private final RequestMetadata requestMetadata;

    public OnUserLoginEvent(@NonNull UserIdentity userInfo,
                            @NonNull RequestMetadata requestMetadata) {
        super(userInfo);
        this.userInfo = userInfo;
        this.requestMetadata = requestMetadata;
    }

    @NonNull
    public UserIdentity getUserInfo() {
        return userInfo;
    }

    @NonNull
    public RequestMetadata getRequestMetadata() {
        return requestMetadata;
    }
}
