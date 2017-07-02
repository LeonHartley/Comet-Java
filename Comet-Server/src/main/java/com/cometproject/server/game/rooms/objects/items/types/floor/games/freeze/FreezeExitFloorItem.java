package com.cometproject.server.game.rooms.objects.items.types.floor.games.freeze;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class FreezeExitFloorItem extends RoomItemFloor {
    public FreezeExitFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);

        this.setExtraData("0");
    }
}
