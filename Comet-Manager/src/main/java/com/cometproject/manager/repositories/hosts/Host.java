package com.cometproject.manager.repositories.hosts;

public class Host {
    private String id;
    private String hostName;
    private String hostEndpoint;
    private String authenticationToken;

    public Host(String id, String hostName, String hostEndpoint, String authenticationToken) {
        this.id = id;
        this.hostName = hostName;
        this.hostEndpoint = hostEndpoint;
        this.authenticationToken = authenticationToken;
    }

    public String getId() {
        return id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getHostEndpoint() {
        return hostEndpoint;
    }

    public void setHostEndpoint(String hostEndpoint) {
        this.hostEndpoint = hostEndpoint;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }
}
