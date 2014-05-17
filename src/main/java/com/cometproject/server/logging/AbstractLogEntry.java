package com.cometproject.server.logging;

public abstract class AbstractLogEntry {
    public abstract LogType getType();
    public abstract String getString();

    public int getRoomId() {
        return -1;
    }

    public int getUserId() {
        return -1;
    }
}
