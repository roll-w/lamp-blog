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

package space.lingu.lamp.web.controller.message;

import space.lingu.lamp.web.domain.message.TransferredMessage;
import space.lingu.lamp.web.ws.AbstractWebSocketMessageConnection;
import space.lingu.lamp.web.ws.UserWebSocketConnectionRegistry;
import space.lingu.lamp.web.ws.WebSocketMessageConnection;

import javax.websocket.Session;

/**
 * @author RollW
 */
public class MessageChatConnection extends AbstractWebSocketMessageConnection<TransferredMessage>
        implements WebSocketMessageConnection<TransferredMessage> {
    private final UserWebSocketConnectionRegistry<MessageChatConnection> registry;

    public MessageChatConnection(Session session,
                                 UserWebSocketConnectionRegistry<MessageChatConnection> registry) {
        super(session);
        this.registry = registry;
    }

    @Override
    public void onConnect() {
    }

    @Override
    public void onDisconnect() {
    }

    @Override
    public void onMessage(TransferredMessage message) {
    }

    @Override
    public void onError(Throwable throwable) {
    }

}
