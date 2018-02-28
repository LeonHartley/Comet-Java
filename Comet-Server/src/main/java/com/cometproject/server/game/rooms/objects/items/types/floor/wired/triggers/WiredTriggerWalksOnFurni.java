package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerWalksOnFurni extends WiredTriggerItem {

    public WiredTriggerWalksOnFurni(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static boolean executeTriggers(RoomEntity entity, RoomItemFloor floorItem) {
        boolean wasExecuted = false;

        for (RoomItemFloor wiredItem : getTriggers(entity.getRoom(), WiredTriggerWalksOnFurni.class)) {
            WiredTriggerWalksOnFurni trigger = ((WiredTriggerWalksOnFurni) wiredItem);

            if (trigger.getWiredData().getSelectedIds().contains(floorItem.getId()))
                wasExecuted = trigger.evaluate(entity, floorItem);
        }

        return wasExecuted;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 1;
    }
}
