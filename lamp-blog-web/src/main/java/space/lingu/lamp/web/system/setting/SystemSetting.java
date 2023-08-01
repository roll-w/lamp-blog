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

package space.lingu.lamp.web.system.setting;

import space.lingu.lamp.DataItem;
import space.lingu.lamp.EntityBuilder;
import space.lingu.lamp.web.domain.systembased.LampSystemResourceKind;
import space.lingu.light.DataColumn;
import space.lingu.light.DataTable;
import space.lingu.light.PrimaryKey;
import tech.rollw.common.web.system.SystemResourceKind;

import java.util.Objects;

/**
 * @author RollW
 */
@DataTable(name = "system_setting")
@SuppressWarnings("all")
public class SystemSetting implements DataItem<SystemSetting, String> {
    @DataColumn(name = "key")
    @PrimaryKey
    private final String key;

    @DataColumn(name = "value")
    private final String value;

    public SystemSetting(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String getId() {
        return key;
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
    public Builder toBuilder() {
        return new Builder(this);
    }

    @Override
    public SystemResourceKind getSystemResourceKind() {
        return LampSystemResourceKind.SYSTEM_SETTING;
    }

    @Override
    public String toString() {
        return "SystemSetting{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemSetting)) return false;
        SystemSetting that = (SystemSetting) o;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    public static final class Builder implements EntityBuilder<SystemSetting, String> {
        private String key;
        private String value;

        public Builder() {
        }

        public Builder(SystemSetting systemSetting) {
            this.key = systemSetting.getKey();
            this.value = systemSetting.getValue();
        }

        @Override
        public Builder setId(String id) {
            return setKey(id);
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        @Override
        public SystemSetting build() {
            return new SystemSetting(key, value);
        }
    }
}
