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
import space.lingu.lamp.web.domain.user.UserIdentity;

import javax.websocket.Session;

/**
 * @author RollW
 */
public abstract class AbstractWebSocketMessageConnection<M>
        implements WebSocketMessageConnection<M> {
    protected final Session session;
    protected final ApiContext apiContext;

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
}
