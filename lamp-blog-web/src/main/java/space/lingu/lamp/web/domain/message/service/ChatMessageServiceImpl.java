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

package space.lingu.lamp.web.domain.message.service;

import org.springframework.stereotype.Service;
import space.lingu.lamp.web.common.ApiContext;
import space.lingu.lamp.web.domain.message.*;
import space.lingu.lamp.user.UserIdentity;
import tech.rollw.common.web.AuthErrorCode;
import tech.rollw.common.web.ErrorCode;
import tech.rollw.common.web.ErrorCodeMessageProvider;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author RollW
 */
@Service
public class ChatMessageServiceImpl implements ChatService, ChatMessageProvider {
    private final ContextThreadAware<ApiContext> apiContextThreadAware;
    private final ErrorCodeMessageProvider errorCodeMessageProvider;


    // TODO: for test, replace with a real database
    private final List<TransferredMessage> messages = new ArrayList<>();

    public ChatMessageServiceImpl(ContextThreadAware<ApiContext> apiContextThreadAware,
                                  ErrorCodeMessageProvider errorCodeMessageProvider) {
        this.apiContextThreadAware = apiContextThreadAware;
        this.errorCodeMessageProvider = errorCodeMessageProvider;
    }

    @Override
    public TransferredMessage sendMessage(TransferredMessage message) {
        TransferredMessage handledMessage = handleMessage(message);
        if (handledMessage.getMessageType().canRecord()) {
            messages.add(handledMessage);
        }
        return handledMessage;
    }

    private TransferredMessage handleMessage(TransferredMessage message) {
        ContextThread<ApiContext> contextThread =
                apiContextThreadAware.getContextThread();
        if (!contextThread.hasContext()) {
            throw new IllegalStateException("No context found");
        }
        ApiContext context = contextThread.getContext();
        UserIdentity userIdentity = context.getUser();
        if (userIdentity == null) {
            return createErrorMessage(
                    message,
                    createErrorMessageContent(
                            AuthErrorCode.ERROR_UNAUTHORIZED_USE,
                            "[Client] Not login.",
                            context.getLocale()
                    )
            );
        }

        return switch (message.getChatType()) {
            case USER -> sendMessageToUser(message);
            case SYSTEM -> sendMessageToSystem(message);
            case GROUP -> sendMessageToGroup(message);
        };
    }

    private TransferredMessage sendMessageToSystem(TransferredMessage message) {
        ApiContext context = apiContextThreadAware.getContextThread().getContext();
        return createErrorMessage(
                message,
                createErrorMessageContent(
                        AuthErrorCode.ERROR_NOT_HAS_ROLE,
                        "[Client] Message sender id not match.",
                        context.getLocale()
                )
        );
    }

    private TransferredMessage sendMessageToUser(TransferredMessage message) {
        ApiContext context = apiContextThreadAware.getContextThread().getContext();
        UserIdentity sender = context.getUser();
        if (message.getSenderId() != sender.getUserId()) {
            return createErrorMessage(
                    message,
                    createErrorMessageContent(
                            AuthErrorCode.ERROR_NOT_HAS_ROLE,
                            "[Client] Message sender id not match.",
                            context.getLocale()
                    )
            );
        }
        if (message.getReceiverId() == 3) {
            return createErrorMessage(
                    message,
                    createErrorMessageContent(
                            AuthErrorCode.ERROR_NOT_HAS_ROLE,
                            "[Client] You have been blocked by the receiver.",
                            context.getLocale()
                    )
            );
        }
        return message.fork(System.currentTimeMillis());
    }

    private TransferredMessage sendMessageToGroup(TransferredMessage message) {
        ApiContext context = apiContextThreadAware.getContextThread()
                .getContext();
        UserIdentity sender = context.getUser();
        // TODO: check if the sender is a member of the group

        return message.fork(System.currentTimeMillis());
    }

    private TransferredMessage createErrorMessage(TransferredMessage transferredMessage,
                                                  ErrorMessageContent content) {
        return transferredMessage.fork(
                TransferredMessageType.ERROR,
                content,
                System.currentTimeMillis()
        );
    }

    private ErrorMessageContent createErrorMessageContent(ErrorCode errorCode,
                                                          String message,
                                                          Locale locale) {
        return new ErrorMessageContent(
                errorCode,
                message,
                errorCodeMessageProvider.getMessage(errorCode, locale)
        );
    }

    @Override
    public List<TransferredMessage> getMessages(Operator user) {
        long userId = user.getOperatorId();
        return messages.stream()
                .filter(message -> message.getChatType() == ChatType.GROUP
                        || message.getSenderId() == userId || message.getReceiverId() == userId)
                .toList();
    }

    @Override
    public List<TransferredMessage> getMessages(Operator user, ChatPartner chatPartner) {
        long userId = user.getOperatorId();
        return messages.stream()
                .filter(message -> message.getChatType() == chatPartner.type())
                .filter(message -> message.getSenderId() == userId && message.getReceiverId() == chatPartner.id()
                        || message.getSenderId() == chatPartner.id() && message.getReceiverId() == userId)
                .toList();
    }
}
