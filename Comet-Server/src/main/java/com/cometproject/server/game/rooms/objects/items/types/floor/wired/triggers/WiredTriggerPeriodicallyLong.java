package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerPeriodicallyLong extends WiredTriggerItem {
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
    public WiredTriggerPeriodicallyLong(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().get(PARAM_TICK_LENGTH) == null) {
            this.getWiredData().getParams().put(PARAM_TICK_LENGTH, 1 / 2 * 10); // 5s
        }

        this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getParams().get(PARAM_TICK_LENGTH) / 2) * 10);
    }

    @Override
    public boolean suppliesPlayer() {
        return false;
    }

    @Override
    public void onTickComplete() {
        this.evaluate(null, null);

        // loop
        this.setTicks(RoomItemFactory.getProcessTime(this.getWiredData().getParams().get(PARAM_TICK_LENGTH) / 2) * 10);
    }

    @Override
    public int getInterface() {
        return 12;
    }
}
