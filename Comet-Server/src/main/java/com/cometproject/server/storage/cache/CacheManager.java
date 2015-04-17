//package com.cometproject.server.storage.cache;
//
//import com.cometproject.server.boot.Comet;
//import com.cometproject.server.storage.cache.config.CacheConfiguration;
//import com.cometproject.server.utilities.Initializable;
//import com.cometproject.server.utilities.JsonFactory;
//import org.apache.log4j.Logger;
//import redis.clients.jedis.Jedis;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//
//public class CacheManager implements Initializable {
//    private static CacheManager cacheManager;
//
//    private boolean enabled;
//    private final Logger log = Logger.getLogger(CacheManager.class.getName());
//
//    private CacheConfiguration cacheConfiguration;
//    private Jedis jedis;
//
//    public CacheManager() {
//        this.enabled = Boolean.parseBoolean((String) Comet.getServer().getConfig().getOrDefault("comet.cache.enabled", "false"));
//    }
//
//    @Override
//    public void initialize() {
//        if(!this.enabled)
//            return;
//
//        // Initializes the config for the cache
//        if(!this.initializeConfig()) {
//            log.error("Failed to load Redis cache configuration, disabling caching");
//
//            this.enabled = false;
//            return;
//        }
//
//        if(!this.initializeJedis()) {
//            log.error("Failed to initialize Redis cluster, disabling caching");
//
//            this.enabled = false;
//            return;
//        }
//
//
//        log.info("Redis caching is enabled");
//    }
//
//    private boolean initializeConfig() {
//        BufferedReader reader = null;
//
//        try {
//            reader = new BufferedReader(new FileReader("./config/cache.json"));
//
//            this.cacheConfiguration = JsonFactory.getInstance().fromJson(reader, CacheConfiguration.class);
//
//            return true;
//        } catch(Exception e) {
//            return false;
//        } finally {
//            if(reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    log.warn("Failed to close BufferedReader", e);
//                }
//            }
//        }
//    }
//
//    private boolean initializeJedis() {
//        try {
//            this.jedis = new Jedis("127.0.0.1");
//
//            if (this.jedis.get("test") == null) {
//                this.jedis.append("test", "yes");
//            } else {
//                System.out.println("Persistence works!");
//            }
//
//            return true;
//        } catch(Exception e) {
//            return false;
//        }
//    }
//
//    public boolean isEnabled() {
//        return this.enabled;
//    }
//
//    public static CacheManager getInstance() {
//        if(cacheManager == null)
//            cacheManager = new CacheManager();
//
//        return cacheManager;
//    }
//
//    public CacheConfiguration getConfiguration() {
//        return cacheConfiguration;
//    }
//}
