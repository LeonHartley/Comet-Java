package com.cometproject.server.storage.cache;

import com.cometproject.api.config.Configuration;
import com.cometproject.api.utilities.Initialisable;
import com.cometproject.api.utilities.JsonUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CacheManager implements Initialisable {
    private static CacheManager cacheManager;
    private final Logger log = Logger.getLogger(CacheManager.class.getName());
    private final String keyPrefix;
    private final String connectionString;
    private boolean enabled;
    private JedisPool jedis;

    public CacheManager() {
        this.enabled = Boolean.parseBoolean((String) Configuration.currentConfig().getOrDefault("comet.cache.enabled", "false"));
        this.keyPrefix = (String) Configuration.currentConfig().getOrDefault("comet.cache.prefix", "comet");
        this.connectionString = (String) Configuration.currentConfig().getOrDefault("comet.cache.connection.url", "");
    }

    public static CacheManager getInstance() {
        if (cacheManager == null)
            cacheManager = new CacheManager();

        return cacheManager;
    }

    @Override
    public void initialize() {
        if (!this.enabled)
            return;

        if (this.connectionString.isEmpty()) {
            log.error("Invalid redis connection string");

            this.enabled = false;
            return;
        }

        // Initializes the com.cometproject.networking.api.config for the cache
        if (!this.initializeConfig()) {
            log.error("Failed to load Redis cache configuration, disabling caching");

            this.enabled = false;
            return;
        }

        if (!this.initializeJedis()) {
            log.error("Failed to initialize Redis cluster, disabling caching");

            this.enabled = false;
            return;
        }

        log.info("Redis caching is enabled");
    }

    private boolean initializeConfig() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader("./config/cache.json"));
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.warn("Failed to close BufferedReader", e);
                }
            }
        }
    }

    private boolean initializeJedis() {
        try {
            final JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(100);

            // Wait 100ms before we fall back to MySQL.
            poolConfig.setMaxWaitMillis(100);

            this.jedis = new JedisPool(this.connectionString);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void put(final String key, CachableObject object) {
        if (this.jedis == null) {
            return;
        }

        try {
            try (final Jedis jedis = this.jedis.getResource()) {
                final long startTime = System.currentTimeMillis();

                // Build the String from the object
                final String objectData = object.toString();

                jedis.set(this.getKey(key), objectData);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            log.error("Error while setting object in Redis with key: " + key + ", type: " +
                    object.getClass().getSimpleName(), e);
        }
    }

    public void putString(final String key, final String value) {
        if (this.jedis == null) {
            return;
        }

        try {
            try (final Jedis jedis = this.jedis.getResource()) {
                jedis.set(this.getKey(key), value);
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            log.error("Error while setting string with key: " + key, e);
        }
    }

    public String getString(String key) {
        try {
            try (final Jedis jedis = this.jedis.getResource()) {
                return jedis.get(this.getKey(key));
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            log.error("Error while reading string from Redis with key: " + key, e);
        }

        return null;
    }

    public <T> T get(final Class<T> clazz, final String key) {
        try {
            try (final Jedis jedis = this.jedis.getResource()) {
                final String data = jedis.get(this.getKey(key));

                // Build the object from the String.
                final T object = JsonUtil.getInstance().fromJson(data, clazz);

                if (object != null) {
                    return object;
                }
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            log.error("Error while reading object from Redis with key: " + key + ", type: " + clazz.getSimpleName(), e);
        }

        return null;
    }

    public boolean exists(final String key) {
        try {
            try (final Jedis jedis = this.jedis.getResource()) {
                return jedis.exists(getKey(key));
            } catch (Exception e) {
                throw e;
            }
        } catch (Exception e) {
            log.error("Error while reading EXISTS from redis, key: " + key, e);
        }

        return false;
    }

    private String getKey(final String key) {
        return this.keyPrefix + "." + key;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}
