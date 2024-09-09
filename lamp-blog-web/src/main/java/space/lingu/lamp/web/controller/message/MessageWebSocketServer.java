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


import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.domain.message.ChatService;
import space.lingu.lamp.web.domain.message.ChatType;
import space.lingu.lamp.web.domain.message.TransferredMessage;
import space.lingu.lamp.user.AttributedUser;
import space.lingu.lamp.user.UserProvider;
import space.lingu.lamp.web.ws.UserWebSocketConnectionRegistry;
import space.lingu.lamp.web.ws.WebSocketContextConfigurator;
import tech.rollw.common.web.system.ContextThreadAware;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author RollW
 */
@Controller
@ServerEndpoint(value = "/api/v1/message/talk",
        configurator = WebSocketContextConfigurator.class,
        decoders = {MessageTextDecodeEncoder.class},
        encoders = {MessageTextDecodeEncoder.class, MessageVoDecodeEncoder.class}
)
public class MessageWebSocketServer {
    private static final Logger logger = LoggerFactory.getLogger(MessageWebSocketServer.class);
    private final UserWebSocketConnectionRegistry<MessageChatConnection> registry;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;
    private final UserProvider userProvider;
    private final ChatService chatService;

    public MessageWebSocketServer(UserWebSocketConnectionRegistry<MessageChatConnection> registry,
                                  ContextThreadAware<ApiContext> apiContextThreadAware,
                                  UserProvider userProvider,
                                  ChatService chatService) {
        this.registry = registry;
        this.apiContextThreadAware = apiContextThreadAware;
        this.userProvider = userProvider;
        this.chatService = chatService;
    }

    @OnOpen
    public void onOpen(Session session,
                       EndpointConfig endpointConfig) throws IOException {
        MessageChatConnection connection = new MessageChatConnection(session, registry);
        connection.onConnect();
        registry.registerConnection(connection);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        ApiContext context = getContextFromSession(session);
        Long currId = extractIdFromContext(context);
        if (currId == null) {
            return;
        }

        MessageChatConnection connection = registry.getConnection(
                currId, session);
        connection.onDisconnect();
        registry.unregisterConnection(connection);
    }

    @OnMessage
    public void onMessage(TransferredMessage message,
                          Session session) {
        ApiContext context = getContextFromSession(session);
        Long currId = extractIdFromContext(context);

        long timestamp = System.currentTimeMillis();

        if (message.getMessageType().isHeartbeat() && currId != null) {
            MessageChatConnection connection =
                    registry.getConnection(currId, session);
            connection.onHeartbeat(timestamp);
            // TODO: timeout check
            return;
        }

        try {
            apiContextThreadAware.getContextThread().setContext(context);
            TransferredMessage returnedMessage =
                    makeViewObject(chatService.sendMessage(message));
            if (returnedMessage.getMessageType().isError()) {
                RemoteEndpoint.Async async = session.getAsyncRemote();
                async.sendObject(returnedMessage);
                return;
            }
            if (currId == null) {
                return;
            }
            Collection<MessageChatConnection> connections =
                    getConnections(returnedMessage);
            connections.forEach(target -> {
                RemoteEndpoint.Async async =
                        target.getSession().getAsyncRemote();
                async.sendObject(returnedMessage);
            });
        } finally {
            apiContextThreadAware.getContextThread().clearContext();
        }
    }

    private TransferredMessage makeViewObject(TransferredMessage message) {
        AttributedUser user =
                userProvider.getUser(message.getSenderId());
        // TODO: check user state that can receive message or not

        return TransferredMessageVo.fork(message, user);
    }

    private Collection<MessageChatConnection> getConnections(
            TransferredMessage message) {
        if (message.getMessageType().isError()) {
            return List.of();
        }
        return switch (message.getChatType()) {
            case USER -> getConnectionsOfUser(message);
            case GROUP -> getConnectionsOfGroup(message);
            case SYSTEM -> List.of();
        };
    }

    private Collection<MessageChatConnection> getConnectionsOfUser(TransferredMessage message) {
        if (message.getSenderId() == message.getReceiverId()) {
            return registry.getConnections(message.getSenderId());
        }
        Collection<MessageChatConnection> senderConnections =
                registry.getConnections(message.getSenderId());
        Collection<MessageChatConnection> targets =
                registry.getConnections(message.getReceiverId());

        List<MessageChatConnection> connections = Lists.newArrayList();
        connections.addAll(senderConnections);
        connections.addAll(targets);
        return connections;
    }

    private Collection<MessageChatConnection> getConnectionsOfGroup(
            TransferredMessage message) {
        if (message.getChatType() != ChatType.GROUP) {
            throw new IllegalArgumentException("Not a group message.");
        }

        List<MessageChatConnection> connections = Lists.newArrayList();

        // for test, we use a pre-defined group id
        connections.addAll(registry.getConnections(1));
        connections.addAll(registry.getConnections(2));
        connections.addAll(registry.getConnections(3));
        connections.addAll(registry.getConnections(4));
        // TODO: replace with a real database
        return connections;
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("MessageWebSocketServer error, session: %s".formatted(
                session.getId()
        ), error);
    }

    private Long extractIdFromContext(ApiContext apiContext) {
        if (apiContext.getUser() == null) {
            return null;
        }
        return apiContext.getUser().getUserId();
    }

    private ApiContext getContextFromSession(Session session) {
        return (ApiContext) session.getUserProperties().get(ApiContext.class.getName());
    }

}
