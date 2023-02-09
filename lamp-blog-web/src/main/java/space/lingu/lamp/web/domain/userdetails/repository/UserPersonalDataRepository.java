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

package space.lingu.lamp.web.domain.userdetails.repository;

import org.springframework.stereotype.Repository;
import space.lingu.lamp.web.database.LampDatabase;
import space.lingu.lamp.web.database.dao.UserPersonalDataDao;
import space.lingu.lamp.web.domain.userdetails.UserPersonalData;

import java.util.List;

/**
 * @author RollW
 */
@Repository
public class UserPersonalDataRepository {
    private final UserPersonalDataDao dao;

    public UserPersonalDataRepository(LampDatabase database) {
        this.dao = database.getUserPersonalDataDao();
    }

    public void insert(UserPersonalData userPersonalData) {
        dao.insert(userPersonalData);
    }

    public void update(UserPersonalData userPersonalData) {
        dao.update(userPersonalData);
    }

    public List<UserPersonalData> getAll() {
        return dao.get();
    }

    public UserPersonalData getById(long userId) {
        return dao.getById(userId);
    }

    public List<UserPersonalData> getByIds(Long[] ids) {
        return dao.getInIds(ids);
    }
}
