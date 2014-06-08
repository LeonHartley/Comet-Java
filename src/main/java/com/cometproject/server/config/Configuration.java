package com.cometproject.server.config;

import com.cometproject.server.boot.Comet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Properties;

public class Configuration {
    private Properties properties;

    public Configuration(String file) {
        try {
            Reader stream = new InputStreamReader(new FileInputStream(file), "UTF-8");

            this.properties = new Properties();

            getProperties().load(stream);
            stream.close();
        } catch (Exception e) {
            Comet.exit("Failed to fetch the server configuration (" + file + ")");
        }
    }

    public String get(String key) {
        return this.getProperties().getProperty(key);
    }

    public String getWithDefault(String key, String def) {
        return this.getProperties().getProperty(key, def);
    }

    public Properties getProperties() {
        return this.properties;
    }
}
