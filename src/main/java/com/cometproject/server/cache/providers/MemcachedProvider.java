package com.cometproject.server.cache.providers;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.cache.CacheProvider;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MemcachedProvider implements CacheProvider {
    private final String memcachedServers = Comet.getServer().getConfig().get("comet.cache.memcached.servers");
    private final int maximumWaitTime = Integer.parseInt(Comet.getServer().getConfig().get("comet.cache.memcached.maxWaitTime"));

    private MemcachedClient memcachedClient = null;

    @Override
    public void init() {
        try {
            this.memcachedClient = new MemcachedClient(AddrUtil.getAddresses(this.memcachedServers));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deinitialize() {

    }

    @Override
    public void put(Object identifier, Object obj) {
        // Identifier must be a string for memcached
        if (identifier instanceof String) {
            this.memcachedClient.add((String)identifier, (5 * 60), obj);
        }
    }

    @Override
    public void put(Object identifier, Object obj, int expires) {
        // Identifier must be a string for memcached
        if (identifier instanceof String) {
            this.memcachedClient.add((String)identifier, expires, obj);
        }
    }

    @Override
    public void put(Object identifier, Object obj, int expires, TimeUnit unit) {
        // Identifier must be a string for memcached
        if (identifier instanceof String) {
            this.memcachedClient.add((String)identifier, Math.round(unit.toSeconds(expires)), obj);
        }
    }

    @Override
    public Object get(Object identifier) {
        // Identifier must be a string for memcached
        if (identifier instanceof String) {
            return this.memcachedClient.get((String)identifier);
        }
        return null;
    }

    @Override
    public boolean exists(Object identifier) {
        // Identifier must be a string for memcached
        if (identifier instanceof String) {
            return this.memcachedClient.get((String)identifier) != null;
        }
        return false;
    }
}
