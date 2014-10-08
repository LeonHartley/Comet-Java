package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;

public class BedFloorItem extends RoomItemFloor {
    public BedFloorItem(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(GenericEntity entity) {
        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.LAY, this.getDefinition().getHeight() + "");

        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(GenericEntity entity) {
        entity.removeStatus(RoomEntityStatus.LAY);
        entity.markNeedsUpdate();
    }
}
