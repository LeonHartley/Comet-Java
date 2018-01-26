package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class PetWaterFloorItem extends RoomItemFloor {
    public PetWaterFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (this.getItemData().getData().equalsIgnoreCase("0")) {
            return;
        }
    }
}
