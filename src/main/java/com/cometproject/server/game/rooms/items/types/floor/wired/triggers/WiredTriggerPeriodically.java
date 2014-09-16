package com.cometproject.server.game.rooms.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredTriggerItem;

public class WiredTriggerPeriodically extends WiredTriggerItem {
    private static final int PARAM_TICK_LENGTH = 0;

    /**
     * The default constructor
     *
     * @param id       The ID of the item
     * @param itemId   The ID of the item definition
     * @param roomId   The ID of the room
     * @param owner    The ID of the owner
     * @param x        The position of the item on the X axis
     * @param y        The position of the item on the Y axis
     * @param z        The position of the item on the z axis
     * @param rotation The orientation of the item
     * @param data     The JSON object associated with this item
     */
    public WiredTriggerPeriodically(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);

        if (this.getWiredData().getParams().get(PARAM_TICK_LENGTH) == null) {
            this.getWiredData().putParam(PARAM_TICK_LENGTH, 2); // 1s
        }

        this.setTicks(this.getWiredData().getParams().get(PARAM_TICK_LENGTH));
    }

    @Override
    public boolean suppliesPlayer() {
        return false;
    }

    @Override
    public void onTickComplete() {
        this.evaluate(null, null);

        // loop
        this.setTicks(this.getWiredData().getParams().get(PARAM_TICK_LENGTH));
    }

    @Override
    public int getInterface() {
        return 6;
    }
}
