package com.cometproject.server.game.rooms.objects.items.types.floor.summer;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;

public class SummerShowerFloorItem extends RoomItemFloor {
    public SummerShowerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        this.setExtraData("0");
        this.sendUpdate();
    }
}
