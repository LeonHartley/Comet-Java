package com.cometproject.server.game.rooms.items.types.floor;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class TeleporterFloorItem extends RoomItemFloor {
    public TeleporterFloorItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {

    }

    protected void toggleDoor(boolean state) {
        if (state)
            this.setExtraData("1");
        else
            this.setExtraData("0");

        this.sendUpdate();
    }

    protected void toggleAnimation(boolean state) {
        if (state)
            this.setExtraData("2");
        else
            this.setExtraData("0");

        this.sendUpdate();
    }
}
