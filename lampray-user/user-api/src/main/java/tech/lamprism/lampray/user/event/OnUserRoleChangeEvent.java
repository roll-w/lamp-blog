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

package tech.lamprism.lampray.user.event;

import org.springframework.context.ApplicationEvent;
import space.lingu.NonNull;
import space.lingu.Nullable;
import tech.lamprism.lampray.user.AttributedUser;
import tech.lamprism.lampray.user.Role;


/**
 * @author RollW
 */
public class OnUserRoleChangeEvent extends ApplicationEvent {
    private final AttributedUser user;
    @Nullable
    private final Role previousRole;
    @NonNull
    private final Role currentRole;

    public OnUserRoleChangeEvent(AttributedUser user,
                                 @Nullable Role previousRole,
                                 @NonNull Role currentRole) {
        super(user);
        this.user = user;
        this.previousRole = previousRole;
        this.currentRole = currentRole;
    }

    public AttributedUser getUser() {
        return user;
    }

    @Nullable
    public Role getPreviousRole() {
        return previousRole;
    }

    @NonNull
    public Role getCurrentRole() {
        return currentRole;
    }
}
