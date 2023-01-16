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

package space.lingu.lamp.web.domain.user.filter;

import space.lingu.NonNull;
import space.lingu.lamp.CommonErrorCode;
import space.lingu.lamp.ErrorCode;

/**
 * @author RollW
 */
public abstract class UserInfoFilterChain implements UserInfoFilter {
    private UserInfoFilterChain next;

    private UserInfoFilterChain() {
    }

    public UserInfoFilterChain(UserInfoFilterChain next) {
        this.next = next;
    }

    @NonNull
    @Override
    public ErrorCode filter(@NonNull UserFilteringInfo userFilteringInfo) {
        ErrorCode code = filterInternal(userFilteringInfo);
        if (code.failed()) {
            return code;
        }
        if (next == null) {
            return CommonErrorCode.SUCCESS;
        }
        return next.filter(userFilteringInfo);
    }

    @NonNull
    protected abstract ErrorCode filterInternal(@NonNull UserFilteringInfo next);

    protected void append(UserInfoFilterChain next) {
        this.next = next;
    }

    public static UserInfoFilter connect(UserInfoFilter... filters) {
        if (filters.length == 0) {
            return EmptyFilterChain.INSTANCE;
        }
        final UserInfoFilterChain head = getChain(filters[0]);
        UserInfoFilterChain current = head;
        for (int i = 1; i < filters.length; i++) {
            current.append(getChain(filters[i]));
            current = current.next;
        }
        return head;
    }

    private static UserInfoFilterChain getChain(UserInfoFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter is null");
        }
        if (filter instanceof UserInfoFilterChain) {
            return (UserInfoFilterChain) filter;
        }
        return new FilterChain(filter);
    }

    private static class FilterChain extends UserInfoFilterChain {
        private final UserInfoFilter filter;

        private FilterChain(UserInfoFilter filter) {
            this.filter = filter;
        }

        @NonNull
        @Override
        protected ErrorCode filterInternal(@NonNull UserFilteringInfo next) {
            return filter.filter(next);
        }
    }

    private static class EmptyFilterChain extends UserInfoFilterChain {
        private static final EmptyFilterChain INSTANCE = new EmptyFilterChain();

        @NonNull
        @Override
        protected ErrorCode filterInternal(@NonNull UserFilteringInfo next) {
            return CommonErrorCode.SUCCESS;
        }
    }
}
