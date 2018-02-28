package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;


public class BedFloorItem extends DefaultFloorItem {
    public BedFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        entity.setBodyRotation(this.getRotation());
        entity.setHeadRotation(this.getRotation());
        entity.addStatus(RoomEntityStatus.LAY, this.getDefinition().getHeight() + "");

        entity.markNeedsUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        entity.removeStatus(RoomEntityStatus.LAY);
        entity.markNeedsUpdate();
    }
}
