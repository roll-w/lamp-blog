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

import space.lingu.lamp.web.domain.message.ChatType;
import space.lingu.lamp.web.domain.message.MessageContent;
import space.lingu.lamp.web.domain.message.TransferredMessage;
import space.lingu.lamp.web.domain.message.TransferredMessageType;
import space.lingu.lamp.user.UserIdentity;

/**
 * @author RollW
 */
public record TransferredMessageVo(
        TransferredMessageType messageType,
        long senderId,
        long receiverId,
        // always the sender's username
        String username,
        ChatType chatType,
        MessageContent content,
        long timestamp
) implements TransferredMessage {
    @Override
    public TransferredMessage fork(long time) {
        return fork(
                messageType == null ? TransferredMessageType.NORMAL : messageType,
                time
        );
    }

    @Override
    public TransferredMessage fork(TransferredMessageType type,
                                   long time) {
        return new TransferredMessageVo(
                type,
                senderId,
                receiverId,
                username,
                chatType,
                content,
                time
        );
    }

    @Override
    public TransferredMessage fork(TransferredMessageType type,
                                   MessageContent messageContent,
                                   long time) {
        return new TransferredMessageVo(
                type,
                senderId,
                receiverId,
                username,
                chatType,
                messageContent,
                time
        );
    }

    @Override
    public TransferredMessageType getMessageType() {
        return messageType;
    }

    @Override
    public long getSenderId() {
        return senderId;
    }

    @Override
    public long getReceiverId() {
        return receiverId;
    }

    @Override
    public ChatType getChatType() {
        return chatType;
    }

    @Override
    public MessageContent getContent() {
        return content;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public TransferredMessage fork() {
        return new TransferredMessageVo(
                messageType,
                senderId,
                receiverId,
                username,
                chatType,
                content,
                timestamp
        );
    }

    public static TransferredMessageVo fork(TransferredMessage transferredMessage,
                                            UserIdentity userIdentity) {
        return new TransferredMessageVo(
                transferredMessage.getMessageType(),
                transferredMessage.getSenderId(),
                transferredMessage.getReceiverId(),
                userIdentity.getUsername(),
                transferredMessage.getChatType(),
                transferredMessage.getContent(),
                transferredMessage.getTimestamp()
        );
    }
}
