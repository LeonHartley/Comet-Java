package com.cometproject.server.cache;

import java.util.concurrent.TimeUnit;

public interface CacheProvider {
    public void init();

    public void deinitialize();

    public void put(String identifier, String obj);

    public void put(String identifer, String obj, int expires);

    public void put(String identifier, String obj, int expires, TimeUnit unit);

    public Object get(String identifier);

    public boolean exists(String identifier);
}
