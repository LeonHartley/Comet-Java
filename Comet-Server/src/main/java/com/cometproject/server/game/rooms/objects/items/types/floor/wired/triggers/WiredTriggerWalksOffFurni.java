package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerWalksOffFurni extends WiredTriggerItem {
    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param room     The instance of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredTriggerWalksOffFurni(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 1;
    }

    public static boolean executeTriggers(RoomEntity entity, RoomItemFloor floorItem) {
        boolean wasExecuted = false;

        for (RoomItemFloor wiredItem : entity.getRoom().getItems().getByClass(WiredTriggerWalksOffFurni.class)) {
            WiredTriggerWalksOffFurni trigger = ((WiredTriggerWalksOffFurni) wiredItem);

            if (trigger.getWiredData().getSelectedIds().contains(floorItem.getId()))
                wasExecuted = trigger.evaluate(entity, floorItem);
        }

        return wasExecuted;
    }
}
