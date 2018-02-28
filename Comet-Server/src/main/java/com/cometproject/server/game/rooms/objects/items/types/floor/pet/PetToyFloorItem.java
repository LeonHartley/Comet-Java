package com.cometproject.server.game.rooms.objects.items.types.floor.pet;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;

public class PetToyFloorItem extends DefaultFloorItem {
    public PetToyFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return;
        }

        entity.setBodyRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.PLAY, "" + entity.getPosition().getZ());
        entity.markNeedsUpdate();

        this.getItemData().setData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return;
        }

        entity.removeStatus(RoomEntityStatus.PLAY);
        entity.markNeedsUpdate();

        this.getItemData().setData("0");
        this.sendUpdate();
    }
}
