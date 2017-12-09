package com.cometproject.common.caching;

import com.cometproject.api.caching.ICache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BiConsumer;

public class LastReferenceCache<TKey, TObj> implements ICache<TKey, TObj> {

    private final Map<TKey, TObj> cache;

    public LastReferenceCache(long objectLifetime, ScheduledExecutorService executorService) {
        this.cache = new ConcurrentHashMap<TKey, TObj>();
    }

    @Override
    public void forEach(BiConsumer consumer) {

    }

    @Override
    public TObj get(TKey tKey) {
        return null;
    }

    @Override
    public void remove(TKey tKey) {

    }

    @Override
    public void add(TKey tKey, TObj tObj) {

    }
}
