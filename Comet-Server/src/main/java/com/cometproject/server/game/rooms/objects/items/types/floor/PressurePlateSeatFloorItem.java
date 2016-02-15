package com.cometproject.server.game.rooms.objects.items.types.floor;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.types.Room;

public class PressurePlateSeatFloorItem extends SeatFloorItem {
    public PressurePlateSeatFloorItem(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        super.onEntityStepOn(entity);

        this.setExtraData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        super.onEntityStepOff(entity);

        this.setExtraData("0");
        this.sendUpdate();
    }
}
