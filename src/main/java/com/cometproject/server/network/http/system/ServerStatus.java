package com.cometproject.server.network.http.system;

public class ServerStatus {
    public int users;
    public int rooms;
    public String uptime;
    public long allocated_memory;
    public long used_memory;
    public String os;
    public int cores;
    private int db_connections;

    public ServerStatus(int _users, int _rooms, String _uptime, long _allocMemory, long _usedMemory, String _os, int _cores, int _dbConnections) {
        this.users = _users;
        this.rooms = _rooms;
        this.uptime = _uptime;
        this.allocated_memory = _allocMemory;
        this.used_memory = _usedMemory;
        this.os = _os;
        this.cores = _cores;
        this.db_connections = _dbConnections;
    }
}