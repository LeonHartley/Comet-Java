package com.cometproject.api.config;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;


public class Configuration extends Properties {
    /**
     * The configuration logger
     */
    private static Logger log = Logger.getLogger(Configuration.class.getName());

    private static Configuration configuration;

    /**
     * Initialize the configuration object
     * This configuration will be loaded from the *.properties files in /com.cometproject.networking.api.config
     *
     * @param file The name of the com.cometproject.networking.api.config file
     */
    public Configuration(String file) {
        super();

        try {
            Reader stream = new InputStreamReader(new FileInputStream(file), "UTF-8");

            this.load(stream);
            stream.close();
        } catch (Exception e) {
            log.error("Failed to fetch the server configuration (" + file + ")");
            System.exit(1);
        }
    }

    /**
     * Override configuration
     *
     * @param config The com.cometproject.networking.api.config strings which you want to override
     */
    public void override(Map<String, String> config) {
        for (Map.Entry<String, String> configOverride : config.entrySet()) {
            if (this.containsKey(configOverride.getKey())) {
                this.remove(configOverride.getKey());
                this.put(configOverride.getKey(), configOverride.getValue());
            } else {
                this.put(configOverride.getKey(), configOverride.getValue());

            }
        }
    }

    /**
     * Get a string from the configuration
     *
     * @param key Retrieve a value from the com.cometproject.networking.api.config by the key
     * @return Value from the configuration
     */
    public String get(String key) {
        return this.getProperty(key);
    }

    public String get(String key, String fallback) {
        if (this.containsKey(key)) {
            return this.get(key);
        }

        return fallback;
    }

    public static Configuration currentConfig() {
        return configuration;
    }

    public static void setConfiguration(Configuration conf) {
        configuration = conf;
    }
}
