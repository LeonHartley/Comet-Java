package com.cometproject.server.game.rooms.objects.items.types.floor.wired.addons;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.types.DefaultFloorItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredAddonPressurePlate extends DefaultFloorItem {
    public WiredAddonPressurePlate(RoomItemData itemData, Room room) {
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
