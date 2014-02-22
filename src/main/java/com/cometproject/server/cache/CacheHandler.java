package com.cometproject.server.cache;

import java.util.concurrent.TimeUnit;

public interface CacheHandler<K, V> {
    public void put(K key, V value);
    public void put(K key, V value, int expires);
    public void put(K key, V value, int expires, TimeUnit unit);
    public V get(K key);
    public boolean exists(K key);

    public CacheProvider getProvider();
}
