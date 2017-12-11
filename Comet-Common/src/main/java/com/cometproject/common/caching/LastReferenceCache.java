package com.cometproject.common.caching;

import com.cometproject.api.caching.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public class LastReferenceCache<TKey, TObj> implements Cache<TKey, TObj> {

    private final Map<TKey, CacheEntry<TObj>> cache;

    private final long objectLifetimeMillis;
    private final BiConsumer<TKey, TObj> expireConsumer;

    private final Future processFuture;

    public LastReferenceCache(long objectLifetimeMillis, long lifetimeCheckDelayMillis,
                              BiConsumer<TKey, TObj> expireConsumer, ScheduledExecutorService executorService) {
        this.cache = new ConcurrentHashMap<>();
        this.expireConsumer = expireConsumer;
        this.objectLifetimeMillis = objectLifetimeMillis;

        this.processFuture = executorService.schedule(this::processExpiredObjects, lifetimeCheckDelayMillis, TimeUnit.MILLISECONDS);
    }

    @Override
    public void forEach(BiConsumer<TKey, TObj> consumer) {
        for(Map.Entry<TKey, CacheEntry<TObj>> entry : cache.entrySet()) {
            consumer.accept(entry.getKey(), entry.getValue().getObject());
        }
    }

    private void processExpiredObjects() {
        for(Map.Entry<TKey, CacheEntry<TObj>> cacheEntry : cache.entrySet()) {
            if(cacheEntry.getValue().hasExpired(this.objectLifetimeMillis)) {
                if(this.expireConsumer != null) {
                    this.expireConsumer.accept(cacheEntry.getKey(), cacheEntry.getValue().getObject());
                }

                this.cache.remove(cacheEntry.getKey());
            }
        }
    }

    @Override
    public TObj get(TKey tKey) {
        return this.cache.get(tKey).getObject();
    }

    @Override
    public void remove(TKey key) {
        this.cache.remove(key);
    }

    @Override
    public void add(TKey key, TObj obj) {
        this.cache.put(key, new CacheEntry<>(obj));
    }

    @Override
    public boolean contains(TKey tKey) {
        return this.cache.containsKey(tKey);
    }

    private class CacheEntry<T> {
        private T obj;
        private long lastAccessed = System.currentTimeMillis();

        public CacheEntry(T obj) {
            this.obj = obj;
        }

        public T getObject() {
            this.lastAccessed = System.currentTimeMillis();

            return obj;
        }

        public boolean hasExpired(long objectLifetime) {
            return (System.currentTimeMillis() - objectLifetime) < lastAccessed;
        }
    }
}
