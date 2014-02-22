package com.cometproject.cache.providers;

import com.cometproject.cache.CacheProvider;

import java.util.concurrent.TimeUnit;

public class MemcachedProvider implements CacheProvider {
    @Override
    public void init() {

    }

    @Override
    public void deinitialize() {

    }

    @Override
    public void put(Object identifier, Object obj) {

    }

    @Override
    public void put(Object identifer, Object obj, int expires) {

    }

    @Override
    public void put(Object identifier, Object obj, int expires, TimeUnit unit) {

    }

    @Override
    public Object get(Object identifier) {
        return null;
    }

    @Override
    public boolean exists(Object identifier) {
        return false;
    }
}
