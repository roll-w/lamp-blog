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

package space.lingu.lamp.web.ws;

import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.user.UserIdentity;

import jakarta.websocket.Session;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author RollW
 */
public abstract class AbstractWebSocketMessageConnection<M>
        implements WebSocketMessageConnection<M> {
    protected final Session session;
    protected final ApiContext apiContext;

    protected final AtomicLong lastHeartbeatTime = new AtomicLong();
    protected final AtomicLong timeout = new AtomicLong(DEFAULT_TIMEOUT);

    public AbstractWebSocketMessageConnection(Session session) {
        this.session = session;
        this.apiContext = initContext(session);
    }

    @Override
    public Session getSession() {
        return session;
    }

    public ApiContext getApiContext() {
        return apiContext;
    }

    private ApiContext initContext(Session session) {
        return (ApiContext) session.getUserProperties()
                .get(ApiContext.class.getName());
    }

    @Override
    public UserIdentity getUser() {
        return apiContext.getUser();
    }

    @Override
    public void onConnect() {
    }

    @Override
    public void onDisconnect() {
    }

    @Override
    public void onMessage(M message) {
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onHeartbeat(long timestamp) {
        lastHeartbeatTime.set(timestamp);
    }

    @Override
    public long getLastHeartbeatTime() {
        return lastHeartbeatTime.get();
    }

    @Override
    public void setTimeout(long timeoutInMillis) {
        timeout.set(timeoutInMillis);
    }

    @Override
    public long getTimeout() {
        return timeout.get();
    }

    @Override
    public boolean isTimeout(long timestamp) {
        return timestamp - lastHeartbeatTime.get() > timeout.get();
    }

    @Override
    public boolean isClosed() {
        return session == null || !session.isOpen();
    }

    @Override
    public void close() {
        if (session == null) {
            return;
        }
        if (!session.isOpen()) {
            return;
        }

        try {
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
            // TODO: replace with WebSocketException.
        }
    }
}
