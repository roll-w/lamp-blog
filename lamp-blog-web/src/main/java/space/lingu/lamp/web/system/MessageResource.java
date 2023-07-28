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

package space.lingu.lamp.web.system;

import space.lingu.lamp.DataItem;
import space.lingu.lamp.EntityBuilder;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.*;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.Locale;

/**
 * Message resource, overrides the default message resource in the system.
 * Could be used to override the default i18n messages in the system.
 *
 * @author RollW
 */
@DataTable(name = "message_resource", indices = {
        @Index(value = {"key", "locale"}, unique = true)
})
public record MessageResource(
        @DataColumn(name = "id")
        @PrimaryKey(autoGenerate = true)
        Long id,

        @DataColumn(name = "key")
        String key,

        @DataColumn(name = "value", dataType = SQLDataType.LONGTEXT)
        String value,

        @DataColumn(name = "locale")
        Locale locale
) implements DataItem<MessageResource> {
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getCreateTime() {
        return 0;
    }

    @Override
    public long getUpdateTime() {
        return 0;
    }

    @Override
    public EntityBuilder<MessageResource> toBuilder() {
        return new Builder(this);
    }

    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.SYSTEM_MESSAGE_RESOURCE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static MessageResource of(String key, String value, Locale locale) {
        return builder()
                .setKey(key)
                .setValue(value)
                .setLocale(locale)
                .build();
    }

    public static final class Builder implements EntityBuilder<MessageResource> {
        private Long id;
        private String key;
        private String value;
        private Locale locale;

        public Builder() {
        }

        public Builder(MessageResource messageResource) {
            this.id = messageResource.id;
            this.key = messageResource.key;
            this.value = messageResource.value;
            this.locale = messageResource.locale;
        }

        @Override
        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        @Override
        public MessageResource build() {
            return new MessageResource(id, key, value, locale);
        }
    }
}
