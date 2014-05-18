package com.cometproject.server.cache.providers;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.cache.CacheProvider;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RedisProvider implements CacheProvider {
    private final String redisServer = Comet.getServer().getConfig().get("comet.cache.redis.host");

    private JedisPool jedisPool;

    @Override
    public void init() {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), redisServer);
    }

    @Override
    public void deinitialize() {

    }

    @Override
    public void put(String identifier, String value) {
        Jedis jedis = this.jedisPool.getResource();

        try {
            jedis.set(identifier, value);
        } catch (JedisConnectionException e) {
            if (jedis != null) {
                this.jedisPool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            if (jedis != null) {
                this.jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public void put(String identifier, String value, int expires) {
    }

    @Override
    public void put(String identifier, String value, int expires, TimeUnit unit) {
    }

    @Override
    public Object get(String identifier) {
        return null;
    }

    @Override
    public boolean exists(String identifier) {
        return false;
    }
}
