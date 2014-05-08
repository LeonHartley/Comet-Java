package com.cometproject.server.game.rooms.models.types;

import com.cometproject.server.game.rooms.models.RoomModel;

public class DynamicRoomModel extends RoomModel {
    public DynamicRoomModel(String name, String heightmap, int doorX, int doorY, int doorZ, int doorRotation) {
        super(name, heightmap, doorX, doorY, doorZ, doorRotation);
    }
}
