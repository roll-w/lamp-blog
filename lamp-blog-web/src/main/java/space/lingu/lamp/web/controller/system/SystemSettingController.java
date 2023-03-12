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

package space.lingu.lamp.web.controller.system;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import space.lingu.lamp.HttpResponseEntity;
import space.lingu.lamp.data.page.PageRequest;
import space.lingu.lamp.web.controller.AdminApi;
import space.lingu.lamp.web.system.setting.SettingProvider;
import space.lingu.lamp.web.system.setting.SystemSetting;

import java.util.List;

/**
 * @author RollW
 */
@AdminApi
public class SystemSettingController {
    private final SettingProvider settingProvider;

    public SystemSettingController(SettingProvider settingProvider) {
        this.settingProvider = settingProvider;
    }

    @GetMapping("/system/settings")
    public HttpResponseEntity<List<SystemSetting>> getSettings(
            PageRequest pageRequest) {
        List<SystemSetting> systemSettings =
                settingProvider.getSettings(pageRequest.getPage(), pageRequest.getSize());
        return HttpResponseEntity.success(systemSettings);
    }

    @PutMapping("/system/setting/{key}")
    public HttpResponseEntity<Void> setSetting(@PathVariable String key,
                                               @RequestBody Value value) {
        settingProvider.setSetting(key, value.value);
        return HttpResponseEntity.success();
    }

    @DeleteMapping("/system/setting/{key}")
    public HttpResponseEntity<Void> deleteSetting(@PathVariable String key) {
        settingProvider.deleteSetting(key);
        return HttpResponseEntity.success();
    }

    @GetMapping("/system/setting/{key}")
    public HttpResponseEntity<SystemSetting> getSetting(@PathVariable String key) {
        SystemSetting setting = settingProvider.getSetting(key);
        return HttpResponseEntity.success(setting);
    }

    public record Value(String value) {
    }
}
