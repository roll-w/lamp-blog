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

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.springframework.stereotype.Service;
import space.lingu.lamp.web.ws.AbstractWebSocketMessageConnection;
import space.lingu.lamp.web.ws.UserWebSocketConnectionRegistry;

import jakarta.websocket.Session;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author RollW
 */
@Service
public class MessageConnectionRegistry implements UserWebSocketConnectionRegistry<MessageChatConnection> {
    private final Multimap<Long, MessageChatConnection> connections =
            Multimaps.newSetMultimap(new ConcurrentHashMap<>(), HashSet::new);

    @Override
    public void registerConnection(MessageChatConnection connection) {
        long hasCurr = connections.get(connection.getUser().getUserId())
                .stream()
                .filter(conn -> Objects.equals(connection.getSession().getId(), conn.getSession().getId()))
                .count();
        if (hasCurr > 0) {
            return;
        }
        connections.put(connection.getUser().getUserId(), connection);
    }

    @Override
    public void unregisterConnection(MessageChatConnection connection) {
        connections.remove(connection.getUser().getUserId(), connection);
    }

    @Override
    public void unregisterConnection(long userId) {
        connections.removeAll(userId);
    }

    @Override
    public void unregisterConnection(long userId, Session session) {
        connections.get(userId).stream()
                .filter(connection -> Objects.equals(connection.getSession().getId(), session.getId()))
                .forEach(connection -> connections.remove(userId, connection));
    }

    @Override
    public MessageChatConnection getConnection(long userId, Session session) {
        return connections.get(userId).stream()
                .filter(connection -> Objects.equals(connection.getSession().getId(), session.getId()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Collection<MessageChatConnection> getConnections(
            long userId) {
        Collection<MessageChatConnection> userConnections =
                connections.get(userId);
        userConnections.stream()
                .filter(AbstractWebSocketMessageConnection::isClosed)
                .forEach(messageChatConnection ->
                        connections.remove(userId, messageChatConnection));
        return connections.get(userId);
    }

    @Override
    public Collection<MessageChatConnection> getConnections() {
        return connections.values();
    }
}
