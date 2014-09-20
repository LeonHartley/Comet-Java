package com.cometproject.server.game.rooms.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;

public class WiredAddonFloorSwitch extends RoomItemFloor {
    public WiredAddonFloorSwitch(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(entity != null && !this.touching(entity)) return;

        this.toggleInteract(true);

        this.sendUpdate();
        this.saveData();
    }
}
