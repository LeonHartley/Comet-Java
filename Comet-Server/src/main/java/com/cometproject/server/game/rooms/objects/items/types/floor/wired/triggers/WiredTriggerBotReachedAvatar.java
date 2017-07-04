package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;

public class WiredTriggerBotReachedAvatar extends WiredTriggerItem {
    /**
     * The default constructor
     *
     * @param id        The ID of the item
     * @param itemId    The ID of the item definition
     * @param room      The instance of the room
     * @param owner     The ID of the owner
     * @param ownerName The username of the owner
     * @param x         The position of the item on the X axis
     * @param y         The position of the item on the Y axis
     * @param z         The position of the item on the z axis
     * @param rotation  The orientation of the item
     * @param data      The JSON object associated with this item
     */
    public WiredTriggerBotReachedAvatar(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 14;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    public static boolean executeTriggers(RoomEntity entity) {
        boolean wasExecuted = false;

        for (RoomItemFloor floorItem : getTriggers(entity.getRoom(), WiredTriggerBotReachedAvatar.class)) {
            WiredTriggerBotReachedAvatar trigger = ((WiredTriggerBotReachedAvatar) floorItem);

            wasExecuted = trigger.evaluate(entity, null);
        }

        return wasExecuted;
    }
}
