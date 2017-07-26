package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class WaterFloorItem extends RoomItemFloor {
    public WaterFloorItem(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(!(entity instanceof PetEntity)) {
            return;
        }

        entity.addStatus(RoomEntityStatus.SWIM, "");
        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if(!(entity instanceof PetEntity)) {
            return;
        }

        entity.removeStatus(RoomEntityStatus.SWIM);
        entity.markNeedsUpdate();
    }
}
