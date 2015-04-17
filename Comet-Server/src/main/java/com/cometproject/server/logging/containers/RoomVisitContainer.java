package com.cometproject.server.logging.containers;

import com.cometproject.server.logging.database.queries.LogQueries;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;

import java.util.List;


public class RoomVisitContainer {
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
