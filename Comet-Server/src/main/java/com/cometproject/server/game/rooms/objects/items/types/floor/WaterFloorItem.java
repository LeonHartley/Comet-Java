package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class WaterFloorItem extends RoomItemFloor {
    public WaterFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            return;
        }

        entity.addStatus(RoomEntityStatus.SWIM, "0");
        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (!(entity instanceof PetEntity)) {
            entity.applyEffect(new PlayerEffect(-1));
            return;
        }

        entity.removeStatus(RoomEntityStatus.SWIM);
        entity.markNeedsUpdate();

    }
}
