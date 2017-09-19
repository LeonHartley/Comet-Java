package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class PetWaterFloorItem extends RoomItemFloor {
    public PetWaterFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(this.getExtraData().equalsIgnoreCase("0")) {
            return;
        }
    }
}
