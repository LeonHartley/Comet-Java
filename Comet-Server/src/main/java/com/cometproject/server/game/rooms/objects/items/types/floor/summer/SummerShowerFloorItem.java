package com.cometproject.server.game.rooms.objects.items.types.floor.summer;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class SummerShowerFloorItem extends RoomItemFloor {
    public SummerShowerFloorItem(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    @Override
    public void onEntityStepOn(RoomEntity entity) {
        this.getItemData().setData("1");
        this.sendUpdate();
    }

    @Override
    public void onEntityStepOff(RoomEntity entity) {
        this.getItemData().setData("0");
        this.sendUpdate();
    }
}
