package com.cometproject.server.config;

import com.cometproject.server.boot.Comet;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

public class Configuration extends Properties {
    private static Logger log = Logger.getLogger(Configuration.class.getName());

    public Configuration(String file) {
        super();

        try {
            Reader stream = new InputStreamReader(new FileInputStream(file), "UTF-8");

            this.load(stream);
            stream.close();
        } catch (Exception e) {
            Comet.exit("Failed to fetch the server configuration (" + file + ")");
        }
    }

    public void override(Map<String, String> config) {
        for(Map.Entry<String, String> configOverride : config.entrySet()) {
            if(this.containsKey(configOverride.getKey())) {
                this.remove(configOverride.getKey());
                this.put(configOverride.getKey(), configOverride.getValue());

                System.out.println(configOverride.getValue());
            } else {
                log.warn("Invalid override config key: " + configOverride.getKey());
            }
        }
    }

    public String get(String key) {
        return this.getProperty(key);
    }
}
