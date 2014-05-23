package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class SeatFloorItem extends RoomItemFloor {
    private boolean isInUse = false;

    public SeatFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        if (this.isInUse) { return; }
        this.isInUse = true;

        double height = this.getDefinition().getHeight();

        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus("sit", String.valueOf(height).replace(',', '.'));
        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        this.isInUse = false;
    }
}
