package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class PetNestFloorItem extends DefaultFloorItem {
    public PetNestFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }
}
