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

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import space.lingu.Nullable;
import space.lingu.lamp.DataItem;
import space.lingu.lamp.web.database.dao.AutoPrimaryBaseDao;
import tech.rollw.common.web.page.Offset;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
public abstract class AutoPrimaryBaseRepository<T extends DataItem<T>> {
    protected final AutoPrimaryBaseDao<T> primaryBaseDao;
    protected final ContextThreadAware<PageableContext> pageableContextThreadAware;
    protected final Cache cache;

    protected AutoPrimaryBaseRepository(AutoPrimaryBaseDao<T> primaryBaseDao,
                                        ContextThreadAware<PageableContext> pageableContextThreadAware,
                                        CacheManager cacheManager) {
        this.primaryBaseDao = primaryBaseDao;
        this.pageableContextThreadAware = pageableContextThreadAware;
        this.cache = getCache(cacheManager);
    }

    private Cache getCache(CacheManager cacheManager) {
        if (cacheManager == null) {
            return null;
        }
        return cacheManager.getCache("TB-" + primaryBaseDao.getTableName());
    }

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

    public void update(T t) {
        primaryBaseDao.update(t);
        cacheResult(t);
    }

    public void update(List<T> ts) {
        primaryBaseDao.update(ts);
        cacheResult(ts);
    }

    public T getById(long id) {
        Cache.ValueWrapper valueWrapper = getWrapper(id);
        if (valueWrapper != null) {
            return (T) valueWrapper.get();
        }
        T t = (T) valueWrapper.get();
        if (t != null) {
            return t;
        }
        T queried = primaryBaseDao.getById(id);
        return cacheResultWithId(queried, id);
    }

    public List<T> getByIds(List<Long> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        CacheResult<T> ts = searchFromCache(ids);
        if (ts.missedIds().isEmpty()) {
            return ts.ts();
        }
        List<T> missed = cacheResult(
                primaryBaseDao.getByIds(ts.missedIds())
        );

        List<T> result = new ArrayList<>(ts.ts());
        result.addAll(missed);

        return result;
    }

    public int count() {
        return primaryBaseDao.count();
    }

    public List<T> get() {
        ContextThread<PageableContext> contextThread =
                pageableContextThreadAware.getContextThread();
        if (!contextThread.hasContext()) {
            return cacheResult(primaryBaseDao.get());
        }
        PageableContext pageableContext = contextThread.getContext();
        return get(pageableContext.toOffset());
    }

    public List<T> get(Offset offset) {
        ContextThread<PageableContext> contextThread =
                pageableContextThreadAware.getContextThread();
        if (contextThread.hasContext()) {
            PageableContext pageableContext = contextThread.getContext();
            long count = getCount();
            pageableContext.setTotal(count);
        }

        return cacheResult(primaryBaseDao.get(offset));
    }

    private long[] calcIds(Offset offset) {
        long[] ids = new long[offset.limit()];
        for (int i = 0; i < offset.limit(); i++) {
            ids[i] = offset.offset() + (long) i;
        }
        return ids;
    }

    public void invalidateCache() {
        if (cache == null) {
            return;
        }
        cache.clear();
    }

    protected final void invalidateCache(T t) {
        if (cache == null) {
            return;
        }
        if (t == null) {
            return;
        }
        if (t.getId() != null) {
            cache.evict(t.getId());
        }
        onInvalidatedCache(t);
    }

    protected void onInvalidatedCache(T t) {
    }

    protected final T cacheResultWithId(T t, long id) {
        if (cache == null) {
            return t;
        }
        cache.put(id, t);
        onCache(t);
        return t;
    }

    protected final T cacheResult(T t) {
        if (t == null) {
            return null;
        }
        if (cache == null) {
            return t;
        }
        if (t.getId() != null) {
            cache.put(t.getId(), t);
        }
        onCache(t);
        return t;
    }

    protected final List<T> cacheResult(List<T> t) {
        if (t == null || t.isEmpty()) {
            return t;
        }

        if (cache == null) {
            return t;
        }
        for (T t1 : t) {
            cacheResult(t1);
        }
        return t;
    }

    protected void onCache(@Nullable T t) {
    }

    private Cache.ValueWrapper getWrapper(long id) {
        if (cache == null) {
            return null;
        }
        return cache.get(id);
    }

    protected T getFromCache(long id) {
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper wrapper = cache.get(id);
        if (wrapper == null) {
            return null;
        }
        return (T) wrapper.get();
    }

    protected CacheResult<T> searchFromCache(List<Long> ids) {
        if (cache == null) {
            return new CacheResult<>(List.of(), ids);
        }
        List<T> ts = new ArrayList<>();
        List<Long> missedIds = new ArrayList<>();
        for (Long id : ids) {
            Cache.ValueWrapper wrapper = cache.get(id);
            if (wrapper == null) {
                missedIds.add(id);
                continue;
            }
            T t = (T) wrapper.get();
            if (t != null) {
                ts.add(t);
            } else {
                missedIds.add(id);
            }
        }
        return new CacheResult<>(ts, missedIds);
    }

    protected record CacheResult<T>(
            List<T> ts,
            List<Long> missedIds
    ) {
    }

    public long getCount() {
        return count();
    }

    protected abstract Class<T> getEntityClass();
}
