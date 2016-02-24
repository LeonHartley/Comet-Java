package com.cometproject.manager.repositories.instances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class Instance {
    @Id
    private String id;

    private String name;
    private Map<String, String> config;

    @JsonIgnore
    private String authKey;

    public Instance(String id, String name, Map<String, String> config, String authKey) {
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

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
