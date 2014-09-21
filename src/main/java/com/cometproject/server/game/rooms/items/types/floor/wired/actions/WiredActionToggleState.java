package com.cometproject.server.game.rooms.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredActionItem;
import com.google.common.collect.Lists;

import java.util.List;

public class WiredActionToggleState extends WiredActionItem {
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
    public WiredActionToggleState(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean requiresPlayer() {
        return false;
    }

    @Override
    public int getInterface() {
        return 8;
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        List<Position3D> tilesToUpdate = Lists.newArrayList();

        for (int itemId : this.getWiredData().getSelectedIds()) {
            final RoomItemFloor floorItem = this.getRoom().getItems().getFloorItem(itemId);

            if (floorItem == null) continue;
            floorItem.onInteract(null, 0, true);
            tilesToUpdate.add(new Position3D(floorItem.getX(), floorItem.getY()));
        }

        for (Position3D tileToUpdate : tilesToUpdate) {
            this.getRoom().getMapping().updateTile(tileToUpdate.getX(), tileToUpdate.getY());
        }

        tilesToUpdate.clear();
        return false;
    }
}
