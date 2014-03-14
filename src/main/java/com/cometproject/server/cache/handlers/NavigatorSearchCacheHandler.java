package com.cometproject.server.cache.handlers;

import com.cometproject.server.cache.CacheHandler;
import com.cometproject.server.cache.CacheProvider;
import com.cometproject.server.game.rooms.types.Room;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Matty on 13/03/2014.
 */
public class NavigatorSearchCacheHandler implements CacheHandler<String, List<Room>> {
    private final CacheProvider provider;

    public NavigatorSearchCacheHandler(CacheProvider provider) {
        this.provider = provider;
    }

    @Override
    public void put(String key, List<Room> value) {
        this.provider.put(key, value);
    }

    @Override
    public void put(String key, List<Room> value, int expires) {
        this.provider.put(key, value, expires);
    }

    @Override
    public void put(String key, List<Room> value, int expires, TimeUnit unit) {
        this.provider.put(key, value, expires, unit);
    }

    @Override
    public List<Room> get(String key) {
        Object o = this.provider.get(key);
        return (List<Room>) o;
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
