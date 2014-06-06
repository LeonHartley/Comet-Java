package com.cometproject.server.logging;

import com.cometproject.server.logging.old.LogType;

public abstract class AbstractLogEntry {
    public abstract LogEntryType getType();
    public abstract String getString();
    public abstract int getTimestamp();

    public int getRoomId() {
        return -1;
    }

    public int getUserId() {
        return -1;
    }
}
