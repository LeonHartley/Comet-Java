package com.cometproject.server.game.rooms.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.effects.UserEffect;
import com.cometproject.server.game.rooms.entities.misc.Position3D;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.items.types.floor.wired.WiredUtil;

public class WiredActionTeleportPlayer extends WiredActionItem {
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
    public WiredActionTeleportPlayer(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean evaluate(GenericEntity entity, Object data) {
        if(entity == null) return false;

        if(this.entity != null) {
            // this action is busy, pls come back later.
            return false;
        }

        this.entity = entity;

        if(this.getWiredData().getDelay() >= 1) {
            this.setTicks(this.getWiredData().getDelay());
        } else {
            this.onTickComplete();
        }

        return true;
    }

    @Override
    public void onTickComplete() {
        int itemId = WiredUtil.getRandomElement(this.getWiredData().getSelectedIds());
        RoomItemFloor item = this.getRoom().getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        Position3D position = new Position3D(item.getX(), item.getY(), item.getHeight());

        entity.applyEffect(new UserEffect(4, 5));
        entity.updateAndSetPosition(position);
        entity.markNeedsUpdate();

        this.entity = null;
    }


    @Override
    public int getInterface() {
        return 0;
    }

    @Override
    public boolean requiresPlayer() {
        return true;
    }
}
