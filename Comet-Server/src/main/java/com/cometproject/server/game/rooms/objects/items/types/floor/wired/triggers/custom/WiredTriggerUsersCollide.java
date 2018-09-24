package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerUsersCollide extends WiredTriggerItem {

    public WiredTriggerUsersCollide(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static boolean executeTriggers(RoomEntity entity) {
        boolean wasExecuted = false;

        for (RoomItemFloor floorItem : getTriggers(entity.getRoom(), WiredTriggerUsersCollide.class)) {
            WiredTriggerUsersCollide trigger = ((WiredTriggerUsersCollide) floorItem);

            wasExecuted = trigger.evaluate(entity, trigger);
        }

        return wasExecuted;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 9;
    }

}
