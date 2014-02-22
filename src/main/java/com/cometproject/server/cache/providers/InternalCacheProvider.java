package com.cometproject.server.cache.providers;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.cache.CacheProvider;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.MemoryUnit;

import java.util.concurrent.TimeUnit;

public class InternalCacheProvider implements CacheProvider {
    private CacheManager cacheMgr;
    private Cache cacheStore;

    private int maxEntries = Integer.parseInt(Comet.getServer().getConfig().get("comet.cache.internal.maxEntries"));
    private int defaultExpireInSeconds = Integer.parseInt(Comet.getServer().getConfig().get("comet.cache.internal.defaultExpireInSeconds"));

    private boolean overflowToOffHeap = Boolean.parseBoolean(Comet.getServer().getConfig().get("comet.cache.internal.overflowToOffHeap"));
    private long maxBytesLocalOffHeap = Long.parseLong(Comet.getServer().getConfig().get("comet.cache.internal.maxBytesLocalOffHeap"));

    @Override
    public void init() {
        this.cacheMgr = CacheManager.create();

        CacheConfiguration cfg = new CacheConfiguration("cache", maxEntries);

        /* YOU MUST RUN THE JVM WITH -XX:MaxDirectMemorySize=2G FOR OVERFLOW TO LOCAL OFF HEAP */
        if (overflowToOffHeap) {
            cfg.overflowToOffHeap(true).maxBytesLocalOffHeap(maxBytesLocalOffHeap, MemoryUnit.GIGABYTES);
        }

        this.cacheStore = new Cache(cfg);
    }

    @Override
    public void deinitialize() {
        this.cacheMgr.shutdown();
    }

    @Override
    public void put(Object identifier, Object obj) {
        this.cacheStore.put(new Element(identifier, obj, defaultExpireInSeconds, defaultExpireInSeconds));
    }

    @Override
    public void put(Object identifier, Object obj, int expires) {
        this.cacheStore.put(new Element(identifier, obj, expires, expires));
    }

    @Override
    public void put(Object identifier, Object obj, int expires, TimeUnit unit) {
        int time = Math.round(unit.toSeconds(expires));
        this.cacheStore.put(new Element(identifier, obj, time, time));
    }

    @Override
    public Object get(Object identifier) {
        Element el = this.cacheStore.get(identifier);
        if (el != null) { return el.getObjectValue(); }

        return null;
    }

    @Override
    public boolean exists(Object identifier) {
        return this.cacheStore.isKeyInCache(identifier);
    }
}
