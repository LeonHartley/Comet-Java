package com.cometproject.server.logging.containers;

import com.cometproject.server.logging.LogManager;
import com.cometproject.server.logging.LogStore;
import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import javolution.util.FastMap;

import java.util.List;
import java.util.Map;

public class RoomVisitContainer {
    private LogStore logStore;

    public RoomVisitContainer(LogStore logStore) {
        this.logStore = logStore;
    }

    public RoomVisitLogEntry put(int playerId, int roomId, long timeEnter) {
        return LogQueries.putRoomVisit(playerId, roomId, (int) timeEnter);
    }

    public void updateExit(RoomVisitLogEntry logEntry) {
        LogQueries.updateRoomEntry(logEntry);
    }

    public List<RoomVisitLogEntry> get(int playerId, int count) {
        return null;
    }
}
