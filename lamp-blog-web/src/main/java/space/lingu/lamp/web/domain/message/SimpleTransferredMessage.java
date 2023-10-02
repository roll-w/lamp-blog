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

package space.lingu.lamp.web.domain.message;

/**
 * Message transferred between a client and server.
 *
 * @author RollW
 */
public record SimpleTransferredMessage(
        TransferredMessageType messageType,
        long senderId,
        long receiverId,
        ChatType chatType,
        MessageContent content,
        long timestamp
) implements TransferredMessage {
    public SimpleTransferredMessage(
            long senderId,
            long receiverId,
            ChatType chatType,
            MessageContent content
    ) {
        this(TransferredMessageType.NORMAL, senderId,
                receiverId, chatType, content, 0);
    }

    /**
     * Create a heartbeat message.
     *
     * @param time timestamp
     * @return heartbeat message
     */
    public static TransferredMessage heartbeat(long time) {
        return new SimpleTransferredMessage(
                TransferredMessageType.HEARTBEAT,
                0,
                0,
                null,
                null,
                time
        );
    }

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
        return new SimpleTransferredMessage(
                type,
                senderId,
                receiverId,
                chatType,
                content,
                time
        );
    }

    @Override
    public TransferredMessage fork(TransferredMessageType type,
                                   MessageContent messageContent,
                                   long time) {
        return new SimpleTransferredMessage(
                type,
                senderId,
                receiverId,
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
        return new SimpleTransferredMessage(
                messageType,
                senderId,
                receiverId,
                chatType,
                content,
                timestamp
        );
    }
}
