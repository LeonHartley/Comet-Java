package com.cometsrv.cache.providers;

import com.cometsrv.cache.CacheProvider;
import net.sf.ehcache.CacheManager;

import java.util.concurrent.TimeUnit;

public class InternalCacheProvider implements CacheProvider {
    private CacheManager cacheMgr;

    @Override
    public void init() {
        this.cacheMgr = CacheManager.create("/cache-cfg.xml");
    }

    @Override
    public void deinitialize() {
        this.cacheMgr.shutdown();
    }

    @Override
    public void put(Object identifier, Object obj) {

    }

    @Override
    public void put(Object identifer, Object obj, Long expires) {

    }

    @Override
    public void put(Object identifier, Object obj, Long expires, TimeUnit unit) {

    }

    @Override
    public Object get(Object identifier) {
        return null;
    }
}
