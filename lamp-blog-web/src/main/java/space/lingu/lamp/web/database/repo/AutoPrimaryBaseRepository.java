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

package space.lingu.lamp.web.database.repo;

import org.springframework.cache.CacheManager;
import space.lingu.Nullable;
import space.lingu.lamp.LongDataItem;
import space.lingu.lamp.web.database.dao.AutoPrimaryBaseDao;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
public abstract class AutoPrimaryBaseRepository<T extends LongDataItem<T>> extends BaseRepository<T, Long> {
    protected final AutoPrimaryBaseDao<T> primaryBaseDao;

    protected AutoPrimaryBaseRepository(AutoPrimaryBaseDao<T> primaryBaseDao,
                                        ContextThreadAware<PageableContext> pageableContextThreadAware,
                                        CacheManager cacheManager) {
        super(primaryBaseDao, pageableContextThreadAware, cacheManager);
        this.primaryBaseDao = primaryBaseDao;
    }

    @Override
    public T insert(T t) {
        if (t == null) {
            throw new NullPointerException("Null entity 'T' in insert.");
        }
        invalidateCache(t);
        long id = primaryBaseDao.insertReturns(t);
        T inserted = t.toBuilder()
                .setId(id)
                .build();

        return cacheResult(inserted);
    }

    @Override
    public List<T> insert(List<T> ts) {
        for (T t : ts) {
            invalidateCache(t);
        }
        long[] ids = primaryBaseDao.insertReturns(ts);
        List<T> inserted = new ArrayList<>(ts.size());
        for (int i = 0; i < ts.size(); i++) {
            inserted.add(
                    ts.get(i).toBuilder()
                            .setId(ids[i])
                            .build()
            );
        }
        return cacheResult(inserted);
    }

    @Override
    protected void onInvalidatedCache(T t) {
    }


    @Override
    protected void onCache(@Nullable T t) {
    }


    @Override
    protected abstract Class<T> getEntityClass();
}
