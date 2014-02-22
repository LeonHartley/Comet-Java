package com.cometproject.cache.handlers;

import com.cometproject.cache.CacheHandler;
import com.cometproject.cache.CacheProvider;
import com.cometproject.game.players.data.PlayerData;

import java.util.concurrent.TimeUnit;

public class PlayerCacheHandler implements CacheHandler<Integer, PlayerData> {
    private final CacheProvider provider;

    public PlayerCacheHandler(CacheProvider provider) {
        this.provider = provider;
    }

    @Override
    public void put(Integer key, PlayerData value) {
        this.provider.put(key, value);
    }

    @Override
    public void put(Integer key, PlayerData value, int expires) {
        this.provider.put(key, value, expires);
    }

    @Override
    public void put(Integer key, PlayerData value, int expires, TimeUnit unit) {
        this.provider.put(key, value, expires, unit);
    }

    @Override
    public PlayerData get(Integer key) {
        Object o = this.provider.get(key);
        return (PlayerData) o;
    }

    @Override
    public boolean exists(Integer key) {
        return this.provider.exists(key);
    }

    @Override
    public CacheProvider getProvider() {
        return this.provider;
    }
}
