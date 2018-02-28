package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerGameEnds extends WiredTriggerItem {

    public WiredTriggerGameEnds(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static boolean executeTriggers(Room room) {
        boolean wasExecuted = false;

        for (RoomItemFloor floorItem : getTriggers(room, WiredTriggerGameEnds.class)) {
            WiredTriggerGameEnds trigger = ((WiredTriggerGameEnds) floorItem);

            wasExecuted = trigger.evaluate(null, null);
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
