package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.GenericEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.types.Room;

public class WiredActionTeleportPlayer extends WiredActionItem {
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
    public WiredActionTeleportPlayer(int id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
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
        if(this.getWiredData() == null || this.getWiredData().getSelectedIds() == null) return;

        int itemId = WiredUtil.getRandomElement(this.getWiredData().getSelectedIds());
        RoomItemFloor item = this.getRoom().getItems().getFloorItem(itemId);

        if(item == null) {
            return;
        }

        Position position = new Position(item.getPosition().getX(), item.getPosition().getY(), item.getPosition().getZ());

        this.entity.applyEffect(new PlayerEffect(4, 5));
        this.entity.updateAndSetPosition(position);
        this.entity.markNeedsUpdate();

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
