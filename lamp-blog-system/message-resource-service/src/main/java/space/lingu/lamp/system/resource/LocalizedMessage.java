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

package space.lingu.lamp.system.resource;

import org.jetbrains.annotations.NotNull;
import space.lingu.NonNull;
import space.lingu.lamp.DataEntity;
import space.lingu.lamp.LongEntityBuilder;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.Index;
import space.lingu.light.PrimaryKey;
import space.lingu.light.SQLDataType;
import tech.rollw.common.web.system.SystemResourceKind;

import java.time.LocalDateTime;
import java.util.Locale;

/**
 * Localized message resource, overrides the default message resource
 * in the system.
 * <p>
 * Could be used to override the default i18n messages in the system.
 *
 * @author RollW
 */
@DataTable(name = "localized_message", indices = {
        @Index(value = {"key", "locale"}, unique = true)
})
public record LocalizedMessage(
        @DataColumn(name = "id")
        @PrimaryKey(autoGenerate = true)
        Long id,

        @DataColumn(name = "key")
        String key,

        @DataColumn(name = "value", dataType = SQLDataType.LONGTEXT)
        String value,

        @DataColumn(name = "locale")
        Locale locale

        //@DataColumn(name = "update_time")
//        LocalDateTime updateTime
) implements LocalizedMessageResource, DataEntity<Long> {
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

    public Builder toBuilder() {
        return new Builder(this);
    }

    @NonNull
    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LocalizedMessageResourceKind.INSTANCE;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static LocalizedMessage of(String key, String value,
                                      Locale locale) {
        return builder()
                .setKey(key)
                .setValue(value)
                .setLocale(locale)
                .build();
    }

    @Override
    @NotNull
    public String getKey() {
        return key;
    }

    @Override
    @NotNull
    public String getValue() {
        return value;
    }

    @Override
    @NotNull
    public Locale getLocale() {
        return locale;
    }

    public static final class Builder implements LongEntityBuilder<LocalizedMessage> {
        private Long id;
        private String key;
        private String value;
        private Locale locale;
        private LocalDateTime updateTime;

        public Builder() {
        }

        public Builder(LocalizedMessage localizedMessage) {
            this.id = localizedMessage.id;
            this.key = localizedMessage.key;
            this.value = localizedMessage.value;
            this.locale = localizedMessage.locale;
            //this.updateTime = localizedMessage.updateTime;
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

        public Builder setUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        @Override
        public LocalizedMessage build() {
            return new LocalizedMessage(id, key, value, locale);
        }
    }
}
