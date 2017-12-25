package com.cometproject.networking.api.config;

import com.cometproject.networking.api.NetworkingContext;

import java.util.Set;

public class NetworkingServerConfig {

    private final String host;
    private final Set<Short> ports;
    private final boolean shouldEncrypt;

    public NetworkingServerConfig(String host, Set<Short> ports, boolean shouldEncrypt) {
        this.host = host;
        this.ports = ports;
        this.shouldEncrypt = shouldEncrypt;
    }

    public NetworkingServerConfig(String host, Set<Short> ports) {
        this(host, ports, true);
    }

    public String getHost() {
        return host;
    }

    public Set<Short> getPorts() {
        return ports;
    }

    public boolean shouldEncrypt() {
        return this.shouldEncrypt;
    }
}
