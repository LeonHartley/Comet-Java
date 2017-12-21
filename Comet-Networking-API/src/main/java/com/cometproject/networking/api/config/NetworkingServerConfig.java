package com.cometproject.networking.api.config;

import java.util.Set;

public class NetworkingServerConfig {

    private final String host;
    private final Set<Short> ports;

    public NetworkingServerConfig(String host, Set<Short> ports) {
        this.host = host;
        this.ports = ports;
    }

    public String getHost() {
        return host;
    }

    public Set<Short> getPorts() {
        return ports;
    }
}
