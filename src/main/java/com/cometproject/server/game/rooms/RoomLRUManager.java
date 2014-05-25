package com.cometproject.server.game.rooms;

import com.cometproject.server.game.rooms.types.RoomData;
import org.apache.commons.collections4.map.LRUMap;

public class RoomLRUManager {
    private final LRUMap<Integer, RoomData> lruRoomMap;

    public RoomLRUManager() {
        // set the max size to the estimated amount of rooms loaded * 2
        lruRoomMap = new LRUMap<>(2000);
    }

    public void store(RoomData data) {
        this.lruRoomMap.put(data.getId(), data);
    }

    public RoomData get(int key) {
        return this.lruRoomMap.getOrDefault(key, null);
    }
}
