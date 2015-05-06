package com.cometproject.manager.repositories.instances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Instance {
    @Id
    private String id;

    private String name;
    private List<ConfigProperty> config;

    @JsonIgnore
    private String authKey;

    public Instance(String id, String name, List<ConfigProperty> config, String authKey) {
        this.id = id;
        this.name = name;
        this.config = config;
        this.authKey = authKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ConfigProperty> getConfig() {
        return config;
    }

    public void setConfig(List<ConfigProperty> config) {
        this.config = config;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    private class ConfigProperty {
        private String key;
        private String value;

        public ConfigProperty(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
