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

package space.lingu.lamp.web.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import space.lingu.lamp.DataItem;
import space.lingu.lamp.web.common.CacheNames;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Supplier;

/**
 * @author RollW
 */
@Component
public class PagesCache {
    private static final Logger logger = LoggerFactory.getLogger(PagesCache.class);

    private final Cache cache;
    private final List<CountableDao<?>> countableDaos;

    public PagesCache(CacheManager cacheManager,
                      List<CountableDao<?>> countableDaos) {
        this.cache = cacheManager.getCache(CacheNames.PAGES);
        this.countableDaos = countableDaos;
    }

    public long getCount(Class<? extends DataItem> type) {
        String key = toCountKey(type);
        CountableDao<?> countableDao = findCountableDao(type);
        if (countableDao == null) {
            logger.error("No CountableDao found for type: {}", type.getName());
            return 0;
        }
        return tryGetCount(key, countableDao::getCount);
    }

    public long getActiveCount(Class<? extends DataItem> type) {
        String key = toActiveKey(type);
        CountableDao<?> countableDao = findCountableDao(type);
        if (countableDao == null) {
            logger.error("No CountableDao found for type: {}", type.getName());
            return 0;
        }
        return tryGetCount(key, countableDao::getActiveCount);
    }

    // allow to set count manually

    public void setCount(Class<? extends DataItem> type, long count) {
        String key = toCountKey(type);
        trySetCount(key, count);
    }

    public void setActiveCount(Class<? extends DataItem> type, long count) {
        String key = toActiveKey(type);
        trySetCount(key, count);
    }

    private long tryGetCount(String key,
                             Supplier<Long> longProvider) {
        AtomicLong atomicLong = cache.get(key, AtomicLong.class);
        if (atomicLong != null) {
            return atomicLong.get();
        }
        long value = longProvider.get();
        cache.put(key, new AtomicLong(value));
        return value;
    }

    private void trySetCount(String key, long value) {
        AtomicLong atomicLong = cache.get(key, AtomicLong.class);
        if (atomicLong != null) {
            atomicLong.set(value);
            return;
        }
        cache.put(key, new AtomicLong(value));
    }

    private CountableDao<?> findCountableDao(Class<? extends DataItem> type) {
        for (CountableDao<?> countableDao : countableDaos) {
            if (countableDao.getCountableType().equals(type)) {
                return countableDao;
            }
        }
        return null;
    }

    private String toActiveKey(Class<?> clz) {
        return clz.getCanonicalName() + ".active";
    }

    private String toCountKey(Class<?> clz) {
        return clz.getCanonicalName() + ".normal";
    }
}
