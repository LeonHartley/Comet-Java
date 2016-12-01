package com.cometproject.manager.repositories.instances;

import com.cometproject.manager.repositories.hosts.InstanceStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.data.annotation.Id;

import java.util.Map;

public class Instance {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Id
    private String id;

    private String name;
    private Map<String, String> config;

    private String server;

    @JsonIgnore
    private String authKey;

    private String version;

    private InstanceStatus instanceStatus = null;
    private Object apiUrl;

    public Instance(String id, String name, Map<String, String> config, String authKey, String server, String version) {
        this.id = id;
        this.name = name;
        this.config = config;
        this.authKey = authKey;
        this.server = server;
        this.version = version;
    }

    public String getConfigData() {
        return gson.toJson(this.config);
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

    public String getServer() {
        return this.server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public InstanceStatus getInstanceStatus() {
        return instanceStatus;
    }

    public void setInstanceStatus(InstanceStatus instanceStatus) {
        this.instanceStatus = instanceStatus;
    }

    public Object getApiUrl() {
        return apiUrl;
    }
}
