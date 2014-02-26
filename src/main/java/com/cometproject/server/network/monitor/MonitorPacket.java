package com.cometproject.server.network.monitor;

public class MonitorPacket {
    private String name;
    private String message;

    public MonitorPacket(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
