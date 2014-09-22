package com.cometproject.server.game.rooms.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.wired.WiredItemSnapshot;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredActionItem;

public class WiredActionMatchToSnapshot extends WiredActionItem {
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
    public WiredActionMatchToSnapshot(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 3;
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        if(this.getWiredData().getSnapshots().size() == 0) {
            return false;
        }

        for(int itemId : this.getWiredData().getSelectedIds()) {
            RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null) continue;

            WiredItemSnapshot itemSnapshot = this.getWiredData().getSnapshots().get(itemId);

            if(itemSnapshot == null) continue;


        }

        return false;
    }
}
