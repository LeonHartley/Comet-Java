package com.cometproject.server.game.rooms.items.types;

import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class GenericFloorItem extends RoomItemFloor {
    public GenericFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }
}
