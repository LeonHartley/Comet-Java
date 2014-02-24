package com.cometproject.server.config;

import com.cometproject.server.boot.Comet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    private Properties properties;

    public Configuration(String file) {
        try {
            InputStream stream = new FileInputStream(file);

            this.properties = new Properties();

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
