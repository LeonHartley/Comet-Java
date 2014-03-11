package com.cometproject.server.cache;

/*
 * Static access a singleton instance of the cache provider
 */
public class CometCache {
    private static CometCacheManager mgr;

    public static void create() {
        mgr = new CometCacheManager();
    }

    public static CometCacheManager getManager() {
        return mgr;
    }
}
