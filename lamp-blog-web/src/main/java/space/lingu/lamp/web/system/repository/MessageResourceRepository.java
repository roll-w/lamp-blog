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

package space.lingu.lamp.web.system.repository;

import org.springframework.stereotype.Repository;
import space.lingu.lamp.data.page.Offset;
import space.lingu.lamp.data.page.Pageable;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.MessageResourceDao;
import space.lingu.lamp.web.system.MessageResource;

import java.util.List;
import java.util.Locale;

/**
 * @author RollW
 */
@Repository
public class MessageResourceRepository {
    private final MessageResourceDao messageResourceDao;

    public MessageResourceRepository(LampDatabase database) {
        this.messageResourceDao = database.getMessageResourceDao();
    }

    public String getValueBy(String key, Locale locale) {
        MessageResource messageResource = messageResourceDao.getByKey(key, locale);
        if (messageResource == null) {
            return null;
        }
        return messageResource.value();
    }

    public MessageResource get(String key, Locale locale) {
        return messageResourceDao.getByKey(key, locale);
    }

    public List<MessageResource> getMessageResources(String key) {
        return messageResourceDao.getByKey(key);
    }

    public List<MessageResource> getMessageResources(Pageable pageable) {
        Offset offset = pageable.toOffset();
        return messageResourceDao.get(offset);
    }

    public void set(String key, String value, Locale locale) {
        MessageResource messageResource = new MessageResource(key, value, locale);
        set(messageResource);
    }

    public void set(MessageResource messageResource) {
        messageResourceDao.insert(messageResource);
    }

    public void delete(String key) {
        messageResourceDao.deleteByKey(key);
    }

    public void delete(String key, Locale locale) {
        messageResourceDao.deleteByKey(key, locale);
    }
}
