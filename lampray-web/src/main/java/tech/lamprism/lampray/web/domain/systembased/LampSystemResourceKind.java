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

package tech.lamprism.lampray.web.domain.systembased;

import tech.rollw.common.web.system.SystemResourceKind;

import java.util.List;

/**
 * @author RollW
 */
public enum LampSystemResourceKind implements SystemResourceKind {
    // system
    USER,
    USER_GROUP("group"),
    USER_GROUP_MEMBER,
    STAFF,
    REGISTER_CODE,
    SYSTEM_NOTICE_BOARD,
    SYSTEM_MESSAGE_RESOURCE,
    SYSTEM_SETTING,
    STORAGE,

    // content
    ARTICLE,
    COMMENT,
    POST,
    IMAGE,
    USER_LIKE,
    CONTENT_METADATA,
    CONTENT_PASSWORD,
    CONTENT_COLLECTION_METADATA,
    REVIEW_JOB,
    TAG,
    TAG_GROUP,
    FAVORITE_GROUP,
    FAVORITE_ITEM,
    BLOCKLIST,

    EMOJI,

    CHAT_MESSAGE,
    CHAT_GROUP,
    CHAT_GROUP_MEMBER;

    private final List<String> aliases;

    LampSystemResourceKind() {
        this.aliases = List.of();
    }

    LampSystemResourceKind(String... aliases) {
        this.aliases = List.of(aliases);
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }
}
