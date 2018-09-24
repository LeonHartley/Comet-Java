package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.custom;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers.WiredTriggerWalksOnFurni;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerLeavesRoom extends WiredTriggerItem {

    public WiredTriggerLeavesRoom(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);
    }

    public static boolean executeTriggers(PlayerEntity playerEntity) {
        boolean wasExecuted = false;

        for (RoomItemFloor wiredItem : getTriggers(playerEntity.getRoom(), WiredTriggerLeavesRoom.class)) {
            WiredTriggerLeavesRoom trigger = ((WiredTriggerLeavesRoom) wiredItem);

            wasExecuted = trigger.evaluate(playerEntity, trigger);
        }

        return wasExecuted;
    }

    @Override
    public boolean suppliesPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 9;
    }
}
