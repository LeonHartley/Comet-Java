package com.cometproject.server.game.rooms.objects.items.types.floor.snowboarding;

import com.cometproject.server.game.rooms.objects.items.types.floor.AdjustableHeightFloorItem;
import com.cometproject.server.game.rooms.types.Room;


public class SnowboardSlopeFloorItem extends AdjustableHeightFloorItem {
    public SnowboardSlopeFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }
}
