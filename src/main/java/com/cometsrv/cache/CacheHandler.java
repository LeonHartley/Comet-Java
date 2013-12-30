package com.cometsrv.cache;

public interface CacheHandler<K, V> {
    public void put(K key, V value);
    public V get(K key);

    public CacheProvider getProvider();
}
