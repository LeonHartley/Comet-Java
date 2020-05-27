package com.cometproject.server.game.rooms.objects.items.types.floor.hollywood;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class HaloTileFloorItem extends RoomItemFloor {
    private int phase = 0;

    public HaloTileFloorItem(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.getItemData().setData("0");
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        if (this.getItemData().getData().equals("1")) return;

        this.getItemData().setData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        if (this.ticksTimer < 1) {
            this.phase = 2;
            this.setTicks(RoomItemFactory.getProcessTime(0.5));
        }
    }

    @Override
    public void onTickComplete() {
        if (this.getItemData().getData().equals("0")) return;

        this.getItemData().setData("0");
        this.sendUpdate();
    }
}
