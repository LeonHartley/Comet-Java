package com.cometproject.server.game.rooms.objects.items.types.floor.banzai;

import com.cometproject.server.game.rooms.objects.items.types.floor.RollableFloorItem;
import com.cometproject.server.game.rooms.types.Room;


public class BanzaiPuckFloorItem extends RollableFloorItem {
    public BanzaiPuckFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }
}
