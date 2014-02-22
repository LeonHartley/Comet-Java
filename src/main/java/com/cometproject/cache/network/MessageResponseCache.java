package com.cometproject.cache.network;

import com.cometproject.network.messages.types.Composer;

public interface MessageResponseCache {
    public Composer tryGetCachedResponse();
}
