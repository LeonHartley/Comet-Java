package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.types.RoomInstance;

public class PressurePlateSeatFloorItem extends SeatFloorItem {
    public PressurePlateSeatFloorItem(int id, int itemId, RoomInstance room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        super.onEntityStepOn(entity);

        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        super.onEntityStepOff(entity);

        this.setExtraData("0");
        this.sendUpdate();
    }
}
