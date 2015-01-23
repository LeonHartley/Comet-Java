package com.cometproject.server.storage.cache.config;

import redis.clients.jedis.HostAndPort;

import java.util.List;
import java.util.Set;

public class CacheConfiguration {
    private Set<HostAndPort> servers;

    public CacheConfiguration(Set<HostAndPort> servers) {
        this.servers = servers;
    }

    public Set<HostAndPort> getServers() {
        return servers;
    }

    public void setServers(Set<HostAndPort> servers) {
        this.servers = servers;
    }
}
