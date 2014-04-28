package com.cometproject.server.cache.handlers;

import com.cometproject.server.cache.CacheHandler;
import com.cometproject.server.cache.CacheProvider;

import java.util.concurrent.TimeUnit;

/**
 * Created by Matty on 22/04/2014.
 */
public class ComposerCacheHandler implements CacheHandler<String, byte[]> {
    private final CacheProvider provider;

    public ComposerCacheHandler(CacheProvider provider) {
        this.provider = provider;
    }

    @Override
    public void put(String key, byte[] value) {
        this.provider.put(key, value);
    }

    @Override
    public void put(String key, byte[] value, int expires) {
        this.provider.put(key, value, expires);
    }

    @Override
    public void put(String key, byte[] value, int expires, TimeUnit unit) {
        this.provider.put(key, value, expires, unit);
    }

    @Override
    public byte[] get(String key) {
        Object o = this.provider.get(key);
        return (byte[]) o;
    }

    @Override
    public boolean exists(String key) {
        return this.provider.exists(key);
    }

    @Override
    public CacheProvider getProvider() {
        return this.provider;
    }
}
