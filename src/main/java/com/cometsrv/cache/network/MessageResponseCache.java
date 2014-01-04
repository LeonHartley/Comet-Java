package com.cometsrv.cache.network;

import com.cometsrv.network.messages.types.Composer;

public interface MessageResponseCache {
    public Composer tryGetCachedResponse();
}
