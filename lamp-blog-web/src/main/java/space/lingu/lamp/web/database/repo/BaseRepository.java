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
import space.lingu.Dangerous;
import space.lingu.InfoPolicy;
import space.lingu.Nullable;
import space.lingu.lamp.DataItem;
import space.lingu.lamp.web.database.dao.LampDao;
import tech.rollw.common.web.page.Offset;
import tech.rollw.common.web.system.ContextThread;
import tech.rollw.common.web.system.ContextThreadAware;
import tech.rollw.common.web.system.paged.PageableContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author RollW
 */
public abstract class BaseRepository<T extends DataItem<T, ID>, ID> {
    protected final LampDao<T, ID> lampDao;
    protected final ContextThreadAware<PageableContext> pageableContextThreadAware;
    protected final Cache cache;

    protected BaseRepository(LampDao<T, ID> lampDao,
                             ContextThreadAware<PageableContext> pageableContextThreadAware,
                             CacheManager cacheManager) {
        this.lampDao = lampDao;
        this.pageableContextThreadAware = pageableContextThreadAware;
        this.cache = getCache(cacheManager);
    }

    private Cache getCache(CacheManager cacheManager) {
        if (cacheManager == null) {
            return null;
        }
        return cacheManager.getCache("TB-" + lampDao.getTableName());
    }

    public T insert(T t) {
        if (t == null) {
            throw new NullPointerException("Null entity 'T' in insert.");
        }
        invalidateCache(t);
        lampDao.insert(t);
        return cacheResult(t);
    }

    public List<T> insert(List<T> ts) {
        for (T t : ts) {
            invalidateCache(t);
        }
        lampDao.insert(ts);
        return cacheResult(ts);
    }

    public void update(T t) {
        lampDao.update(t);
        cacheResult(t);
    }

    public void update(List<T> ts) {
        lampDao.update(ts);
        cacheResult(ts);
    }

    public T getById(ID id) {
        Cache.ValueWrapper valueWrapper = getWrapper(id);
        if (valueWrapper != null) {
            return (T) valueWrapper.get();
        }
        T queried = lampDao.getById(id);
        return cacheResultWithId(queried, id);
    }

    public List<T> getByIds(List<ID> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        CacheResult<T, ID> ts = searchFromCache(ids);
        if (ts.missedIds().isEmpty()) {
            return ts.data();
        }
        List<T> missed = cacheResult(
                lampDao.getByIds(ts.missedIds())
        );

        List<T> result = new ArrayList<>(ts.data());
        result.addAll(missed);

        return result;
    }

    public int count() {
        return lampDao.count();
    }

    public List<T> get() {
        ContextThread<PageableContext> contextThread =
                pageableContextThreadAware.getContextThread();
        if (!contextThread.hasContext()) {
            return cacheResult(lampDao.get());
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

        return cacheResult(lampDao.get(offset));
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

    protected final T cacheResultWithId(T t, ID id) {
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

    protected final Cache.ValueWrapper getWrapper(ID id) {
        if (cache == null) {
            return null;
        }
        return cache.get(id);
    }

    protected T getFromCache(ID id) {
        if (cache == null) {
            return null;
        }
        Cache.ValueWrapper wrapper = cache.get(id);
        if (wrapper == null) {
            return null;
        }
        return (T) wrapper.get();
    }

    protected CacheResult<T, ID> searchFromCache(List<ID> ids) {
        if (cache == null) {
            return new CacheResult<>(List.of(), ids);
        }
        List<T> ts = new ArrayList<>();
        List<ID> missedIds = new ArrayList<>();
        for (ID id : ids) {
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

    @Dangerous(policy = InfoPolicy.CALLER,
            message = "Verify that you confirm permanent deletion from the database.")
    public void deleteById(ID id) {
        if (id == null) {
            return;
        }
        T t = getFromCache(id);
        if (t != null) {
            invalidateCache(t);
            lampDao.delete(t);
            return;
        }
        lampDao.deleteById(id);
    }

    protected record CacheResult<T, ID>(
            List<T> data,
            List<ID> missedIds
    ) {
    }

    public long getCount() {
        return count();
    }

    protected abstract Class<T> getEntityClass();
}
