package com.cometproject.manager.repositories.hosts;

public class Host {
    private String id;
    private String hostName;
    private String endpoint;
    private String authenticationKey;

    public Host(String id, String hostName, String endpoint, String authenticationKey) {
        this.id = id;
        this.hostName = hostName;
        this.endpoint = endpoint;
        this.authenticationKey = authenticationKey;
    }

    public String getId() {
        return id;
    }

    public String getHostName() {
        return hostName;
    }

    public Host setHostName(String hostName) {
        this.hostName = hostName;
        return this;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Host setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public String getAuthenticationKey() {
        return authenticationKey;
    }

    public Host setAuthenticationKey(String authenticationKey) {
        this.authenticationKey = authenticationKey;
        return this;
    }
}
