package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class RollerFloorItem extends RoomItemFloor {
    public RollerFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        System.out.println("On step on roller..");
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {

    }
}
