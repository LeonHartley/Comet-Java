package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.types.Room;

public class PressurePlateSeatFloorItem extends SeatFloorItem {
    public PressurePlateSeatFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        super.onEntityStepOn(entity);

        this.getItemData().setData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        super.onEntityStepOff(entity);

        this.getItemData().setData("0");
        this.sendUpdate();
    }
}
