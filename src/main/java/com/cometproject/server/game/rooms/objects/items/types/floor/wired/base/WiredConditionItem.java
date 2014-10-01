package com.cometproject.server.game.rooms.objects.items.types.floor.wired.base;

import com.cometproject.server.game.rooms.objects.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.network.messages.outgoing.room.items.wired.dialog.WiredConditionMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

public abstract class WiredConditionItem extends AbstractWiredItem {
    protected boolean isNegative;

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
    public WiredConditionItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
        this.isNegative = this.getClass().getTypeName().startsWith("WiredNegativeCondition");
    }

    @Override
    public Composer getDialog() {
        return WiredConditionMessageComposer.compose(this);
    }
}
