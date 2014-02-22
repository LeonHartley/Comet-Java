package com.cometproject.config;

import com.cometproject.boot.Comet;

import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private String file;
    private Properties properties;

    public Configuration(String file) {
        this.file = file;

        InputStream stream = Configuration.class.getResourceAsStream(this.file);
        this.properties = new Properties();

        try {
            getProperties().load(stream);
            stream.close();
        } catch(Exception e) {
            Comet.exit("Failed to fetch the server configuration (" + file + ")");
        }
    }

    public String get(String key) {
        return this.getProperties().getProperty(key);
    }

    public Properties getProperties() {
        return this.properties;
    }
}
