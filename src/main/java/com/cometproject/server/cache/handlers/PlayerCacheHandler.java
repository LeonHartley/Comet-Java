package com.cometproject.server.cache.handlers;

import com.cometproject.server.cache.CacheHandler;
import com.cometproject.server.cache.CacheProvider;
import com.cometproject.server.game.players.data.PlayerData;

import java.util.concurrent.TimeUnit;

public class PlayerCacheHandler implements CacheHandler<String, PlayerData> {
    private final CacheProvider provider;

    public PlayerCacheHandler(CacheProvider provider) {
        this.provider = provider;
    }

    @Override
    public void put(String key, PlayerData value) {
        this.provider.put(key, value);
    }

    @Override
    public void put(String key, PlayerData value, int expires) {
        this.provider.put(key, value, expires);
    }

    @Override
    public void put(String key, PlayerData value, int expires, TimeUnit unit) {
        this.provider.put(key, value, expires, unit);
    }

    @Override
    public PlayerData get(String key) {
        Object o = this.provider.get(key);
        return (PlayerData) o;
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
