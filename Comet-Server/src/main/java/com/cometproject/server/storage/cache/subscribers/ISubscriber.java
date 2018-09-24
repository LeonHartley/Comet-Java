package com.cometproject.server.storage.cache.subscribers;

import redis.clients.jedis.JedisPool;

public interface ISubscriber {
    void setJedis(JedisPool jedis);

    void subscribe();

    void handleMessage(String message);

    String getChannel();
}
