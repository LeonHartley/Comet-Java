package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerAtGivenTimeLong extends WiredTriggerAtGivenTime {
    private static final int PARAM_TICK_LENGTH = 0;

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
    public WiredTriggerAtGivenTimeLong(RoomItemData roomItemData, Room room) {
        super(roomItemData, room);

        this.getWiredData().getParams().putIfAbsent(PARAM_TICK_LENGTH, 2); // 1s
    }

    public static boolean executeTriggers(Room room, int timer) {
        boolean wasExecuted = false;

        for (RoomItemFloor wiredItem : getTriggers(room, WiredTriggerAtGivenTimeLong.class)) {
            WiredTriggerAtGivenTimeLong trigger = ((WiredTriggerAtGivenTimeLong) wiredItem);

            if (timer >= trigger.getTime()) {
                wasExecuted = trigger.evaluate(null, null);
            }
        }

        return wasExecuted;
    }

    @Override
    public int getTime() {
        return this.getWiredData().getParams().get(PARAM_TICK_LENGTH) * 10;
    }
}
