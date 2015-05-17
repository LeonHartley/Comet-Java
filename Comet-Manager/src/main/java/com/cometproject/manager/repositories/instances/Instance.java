package com.cometproject.manager.repositories.instances;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Instance {
    @Id
    private String id;

    private String name;
    private List<InstanceConfigProperty> config;

    @JsonIgnore
    private String authKey;

    public Instance(String id, String name, List<InstanceConfigProperty> config, String authKey) {
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

    public List<InstanceConfigProperty> getConfig() {
        return config;
    }

    public void setConfig(List<InstanceConfigProperty> config) {
        this.config = config;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
}
