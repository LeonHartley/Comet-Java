package com.cometproject.server.logging;

import java.util.List;

public abstract class AbstractLoggerStore {
    public abstract List<AbstractLogEntry> getByTimeFrame(long min, long max);
    public abstract List<AbstractLogEntry> getByRoomId(int roomId);
    public abstract List<AbstractLogEntry> getByUserId(int userId);

    public abstract void put(AbstractLogEntry entry);
}
