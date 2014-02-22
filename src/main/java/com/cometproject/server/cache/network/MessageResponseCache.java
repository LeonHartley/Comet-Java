package com.cometproject.server.cache.network;

import com.cometproject.server.network.messages.types.Composer;

public interface MessageResponseCache {
    public Composer tryGetCachedResponse();
}
