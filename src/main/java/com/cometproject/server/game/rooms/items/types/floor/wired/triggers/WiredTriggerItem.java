package com.cometproject.server.game.rooms.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.types.floor.wired.AbstractWiredItem;
import com.cometproject.server.game.wired.misc.WiredSquare;
import com.cometproject.server.network.messages.outgoing.room.items.wired.WiredTriggerMessageComposer;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public abstract class WiredTriggerItem extends AbstractWiredItem {
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
    public WiredTriggerItem(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    public void execute(Object data, List<GenericEntity> entities, WiredSquare wiredBlock) {
        //tits
    }

    @Override
    public Composer getDialog() {
        return WiredTriggerMessageComposer.compose(this);
    }
}
