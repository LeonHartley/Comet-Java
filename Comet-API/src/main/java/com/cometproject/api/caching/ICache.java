package com.cometproject.api.caching;

import java.util.Map;
import java.util.function.BiConsumer;

public interface ICache<TKey, TObj> {

    TObj get(TKey key);

    void remove(TKey key);

    void add(TKey key, TObj obj);

    void forEach(BiConsumer<TKey, TObj> consumer);
}
