package com.cometsrv.cache;

import com.cometsrv.cache.handlers.PlayerCacheHandler;
import com.cometsrv.cache.providers.InternalCacheProvider;

public class CometCacheManager {
    private CacheProvider masterProvider;

    private PlayerCacheHandler playerCache;

    public CometCacheManager() {
        this.masterProvider = new InternalCacheProvider();
        this.playerCache = new PlayerCacheHandler(this.masterProvider);
    }

    public PlayerCacheHandler getPlayerCache() {
        return this.playerCache;
    }
}
