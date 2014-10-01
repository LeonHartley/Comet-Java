package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class WiredAddonFloorSwitch extends RoomItemFloor {
    public WiredAddonFloorSwitch(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onInteract(GenericEntity entity, int requestData, boolean isWiredTrigger) {
        if(entity != null) {
            if(!this.getPosition().touching(entity)) {
                entity.moveTo(this.getPosition().squareBehind(this.getRotation()).getX(), this.getPosition().squareBehind(this.getRotation()).getY());
                return;
            }
        }

        this.toggleInteract(true);

        this.sendUpdate();
        this.saveData();
    }
}
