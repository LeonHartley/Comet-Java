package com.cometproject.tools.packets;

public abstract class Packet {
    private short id;
    private String className;
    private PacketType type;

    public Packet(short id, String className, PacketType type) {
        this.id = id;
        this.className = className;
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public PacketType getType() {
        return type;
    }

    public enum PacketType {
        COMPOSER, EVENT
    }
}
