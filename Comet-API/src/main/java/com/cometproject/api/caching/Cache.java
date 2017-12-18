package com.cometproject.api.caching;

import java.util.Map;
import java.util.function.BiConsumer;

public interface Cache<TKey, TObj> {

    TObj get(TKey key);

    void remove(TKey key);

    void add(TKey key, TObj obj);

    boolean contains(TKey key);

    void forEach(BiConsumer<TKey, TObj> consumer);
}
