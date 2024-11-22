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

package tech.lamprism.lampray.web.ws;

import tech.lamprism.lampray.user.UserIdentity;

import jakarta.websocket.Session;

/**
 * @author RollW
 */
public interface WebSocketMessageConnection<M> {
    long DEFAULT_TIMEOUT = 60 * 1000;

    /**
     * Get the session of this connection.
     */
    Session getSession();

    /**
     * Who holds this connection.
     */
    UserIdentity getUser();

    void onConnect();

    void onDisconnect();

    void onMessage(M message);

    void onError(Throwable throwable);

    boolean isClosed();

    void close();

    void onHeartbeat(long timestamp);

    long getLastHeartbeatTime();

    void setTimeout(long timeoutInMillis);

    long getTimeout();

    boolean isTimeout(long timestamp);
}
