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

package space.lingu.lamp.web.database.dao;

import space.lingu.lamp.DataItem;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import tech.rollw.common.web.page.Offset;

import java.util.List;

/**
 * @author RollW
 */
public interface AutoPrimaryBaseDao<T extends DataItem<T>> extends LampDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertReturns(T t);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long[] insertReturns(List<T> ts);

    default T getById(long id) {
        return null;
    }

    default List<T> getByIds(List<Long> ids) {
        return List.of();
    }

    @Override
    default List<T> get() {
        return LampDao.super.get();
    }

    @Override
    default int count() {
        return LampDao.super.count();
    }

    @Override
    default List<T> get(Offset offset) {
        return LampDao.super.get(offset);
    }

    @Override
    default String getTableName() {
        return LampDao.super.getTableName();
    }
}
