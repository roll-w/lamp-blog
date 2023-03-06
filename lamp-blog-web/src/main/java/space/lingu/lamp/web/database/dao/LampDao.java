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

import space.lingu.Dangerous;
import space.lingu.InfoPolicy;
import space.lingu.light.Delete;
import space.lingu.light.Insert;
import space.lingu.light.OnConflictStrategy;
import space.lingu.light.Update;

import java.util.List;

/**
 * @author RollW
 */
public interface LampDao<T> {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(T t);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertWithReturn(T t);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(List<T> ts);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(T t);

    @Update(onConflict = OnConflictStrategy.ABORT)
    void update(List<T> ts);

    @Delete
    @Dangerous(policy = InfoPolicy.CALLER,
            message = "This method is dangerous, please check the caller.")
    void delete(T t);

    @Delete
    @Dangerous(policy = InfoPolicy.CALLER,
            message = "This method is dangerous, please check the caller.")
    void delete(List<T> ts);
}
