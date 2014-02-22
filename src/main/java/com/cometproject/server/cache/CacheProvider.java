package com.cometproject.server.cache;

import java.util.concurrent.TimeUnit;

public interface CacheProvider {
    public void init();
    public void deinitialize();

    public void put(Object identifier, Object obj);
    public void put(Object identifer, Object obj, int expires);
    public void put(Object identifier, Object obj, int expires, TimeUnit unit);

    public Object get(Object identifier);

    public boolean exists(Object identifier);
}
