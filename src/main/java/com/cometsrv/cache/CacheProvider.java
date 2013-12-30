package com.cometsrv.cache;

import java.util.concurrent.TimeUnit;

public interface CacheProvider {
    public void init();
    public void deinitialize();

    public void put(Object identifier, Object obj);
    public void put(Object identifer, Object obj, Long expires);
    public void put(Object identifier, Object obj, Long expires, TimeUnit unit);

    public Object get(Object identifier);
}
