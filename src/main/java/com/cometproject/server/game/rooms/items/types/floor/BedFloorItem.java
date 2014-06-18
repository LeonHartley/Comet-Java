package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class BedFloorItem extends RoomItemFloor {
    public BedFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        // TODO: redirection
        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus("lay", "0.0");

        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        entity.removeStatus("lay");
        entity.markNeedsUpdate();
    }
}
