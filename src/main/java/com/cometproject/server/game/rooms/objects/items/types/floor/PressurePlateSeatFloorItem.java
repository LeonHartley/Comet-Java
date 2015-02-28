package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.items.types.GenericFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class PressurePlateSeatFloorItem extends GenericFloorItem {
    public PressurePlateSeatFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }
}
