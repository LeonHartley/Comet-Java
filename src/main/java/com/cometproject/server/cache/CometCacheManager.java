package com.cometproject.server.cache;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.cache.handlers.ComposerCacheHandler;
import com.cometproject.server.cache.providers.RedisProvider;

public class CometCacheManager {
    private String provider = Comet.getServer().getConfig().get("comet.cache.provider");

    private CacheProvider masterProvider;
    private boolean isCacheEnabled;

    private ComposerCacheHandler composerCacheHandler;

    public CometCacheManager() {
        if ("disabled".equals(provider) || "".equals(provider) || "default".equals(provider)) {
            this.isCacheEnabled = false;
        } else {
            this.isCacheEnabled = true;

            if ("redis".equals(provider)) {
                this.masterProvider = new RedisProvider();
                this.masterProvider.init();
            }

            this.composerCacheHandler = new ComposerCacheHandler(this.masterProvider);
        }
    }

    public boolean isCacheEnabled() {
        return this.isCacheEnabled;
    }

    public ComposerCacheHandler getComposerCacheHandler() {
        return this.composerCacheHandler;
    }
}
