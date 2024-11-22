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

package tech.lamprism.lampray.web.common;

import org.springframework.stereotype.Component;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.DefaultContextThread;

/**
 * @author RollW
 */
@Component
public class ApiContextFactory implements ContextThreadAware<ApiContext> {
    private final ThreadLocal<ContextThread<ApiContext>> apiContextThreadLocal
            = new ThreadLocal<>();

    @Override
    public ContextThread<ApiContext> assambleContextThread(ApiContext context) {
        ContextThread<ApiContext> contextThread = apiContextThreadLocal.get();
        if (contextThread == null) {
            contextThread = new DefaultContextThread<>(context);
            apiContextThreadLocal.set(contextThread);
        }
        contextThread.setContext(context);
        return contextThread;
    }

    @Override
    public ContextThread<ApiContext> getContextThread() {
        ContextThread<ApiContext> thread =
                apiContextThreadLocal.get();
        if (thread != null) {
            return thread;
        }
        return assambleContextThread(null);
    }

    @Override
    public void clearContextThread() {
        apiContextThreadLocal.remove();
    }
}
