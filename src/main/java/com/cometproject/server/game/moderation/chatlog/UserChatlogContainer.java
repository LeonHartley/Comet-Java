package com.cometproject.server.game.moderation.chatlog;

import com.cometproject.server.logging.entries.RoomChatLogEntry;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserChatlogContainer {
    private Map<Integer, List<RoomChatLogEntry>> logs;

    public UserChatlogContainer() {
        this.logs = new FastMap<>();
    }

    public void addAll(int roomId, List<RoomChatLogEntry> chatlogs) {
        this.logs.put(roomId, chatlogs);
    }

    public void add(RoomChatLogEntry logEntry) {
        if(!this.logs.containsKey(logEntry.getRoomId())) {
            this.logs.put(logEntry.getRoomId(), new ArrayList<>());
        }

        this.logs.get(logEntry.getRoomId()).add(logEntry);
    }

    public void dispose() {
        for(List<RoomChatLogEntry> entries : logs.values()) {
            entries.clear();
        }

        this.logs.clear();
    }

    public List<RoomChatLogEntry> getRoom(int id) {
        return this.logs.get(id);
    }

    public int size() {
        return logs.size();
    }

    public int size(int roomId) {
        if(!this.logs.containsKey(roomId))
            return 0;

        return logs.get(roomId).size();
    }

    public Map<Integer, List<RoomChatLogEntry>> getLogs() {
        return this.logs;
    }
}
