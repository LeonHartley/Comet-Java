package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class PetToyFloorItem extends DefaultFloorItem {
    public PetToyFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if(!(entity instanceof PetEntity)) {
            return;
        }

        entity.setBodyRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.PLAY, "" + entity.getPosition().getZ());
        entity.markNeedsUpdate();

        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if(!(entity instanceof PetEntity)) {
            return;
        }

        entity.removeStatus(RoomEntityStatus.PLAY);
        entity.markNeedsUpdate();

        this.setExtraData("0");
        this.sendUpdate();
    }
}
