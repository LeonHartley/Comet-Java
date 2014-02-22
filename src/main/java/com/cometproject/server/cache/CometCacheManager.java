package com.cometproject.server.cache;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.cache.handlers.PlayerCacheHandler;
import com.cometproject.server.cache.providers.InternalCacheProvider;

public class CometCacheManager {
    private String provider = Comet.getServer().getConfig().get("comet.cache.provider");

    private CacheProvider masterProvider;
    private boolean isCacheEnabled;

    private PlayerCacheHandler playerCache;

    public CometCacheManager() {
        if ("disabled".equals(provider)) {
            this.isCacheEnabled = false;
        } else {
            this.isCacheEnabled = true;

            if ("default".equals(provider) || "internal".equals(provider) || "".equals(provider)) {
                this.masterProvider = new InternalCacheProvider();
            }

            this.playerCache = new PlayerCacheHandler(this.masterProvider);
        }
    }

    public boolean isCacheEnabled() {
        return this.isCacheEnabled;
    }

    public PlayerCacheHandler getPlayerCache() {
        return this.playerCache;
    }
}
