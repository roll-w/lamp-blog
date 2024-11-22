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

package tech.lamprism.lampray.web.controller.message;

import org.springframework.web.bind.annotation.GetMapping;
import tech.lamprism.lampray.web.common.ApiContext;
import tech.lamprism.lampray.web.controller.Api;
import tech.lamprism.lampray.web.domain.message.ChatMessageProvider;
import tech.lamprism.lampray.web.domain.message.TransferredMessage;
import tech.lamprism.lampray.user.UserIdentity;
import tech.rollw.common.web.HttpResponseEntity;
import tech.rollw.common.web.system.ContextThreadAware;

import java.util.List;

/**
 * @author RollW
 */
@Api
public class ChatMessageController {
    private final ChatMessageProvider chatMessageProvider;
    private final ContextThreadAware<ApiContext> apiContextThreadAware;

    public ChatMessageController(ChatMessageProvider chatMessageProvider,
                                 ContextThreadAware<ApiContext> apiContextThreadAware) {
        this.chatMessageProvider = chatMessageProvider;
        this.apiContextThreadAware = apiContextThreadAware;
    }

    @GetMapping("/messages")
    public HttpResponseEntity<List<TransferredMessage>> getMessages() {
        ApiContext context =
                apiContextThreadAware.getContextThread().getContext();
        UserIdentity user = context.getUser();
        List<TransferredMessage> messages = chatMessageProvider.getMessages(user);
        return HttpResponseEntity.success(messages);
    }
}
