package com.cometproject.server.game.rooms.objects.items.types.floor.wired.actions;

import com.cometproject.server.game.rooms.objects.entities.RoomEntity;
import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.WiredUtil;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredActionItem;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.events.WiredItemEvent;
import com.cometproject.server.game.rooms.objects.items.types.state.FloorItemEvent;
import com.cometproject.server.game.rooms.objects.misc.Position;
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
    public WiredActionTeleportPlayer(long id, int itemId, Room room, int owner, String ownerName, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, ownerName, x, y, z, rotation, data);
    }

    @Override
    public boolean evaluate(RoomEntity entity, Object data) {
        if (entity == null) return false;

        final WiredItemEvent event = new WiredItemEvent();

        event.entity = entity;

        if (this.getWiredData().getDelay() >= 1) {
            event.setTotalTicks(RoomItemFactory.getProcessTime(this.getWiredData().getDelay() / 2));
            this.queueEvent(event);
        } else {
            this.onEventComplete(event);
        }

        return true;
    }

    @Override
    public void onEventComplete(WiredItemEvent event) {
        if (event.entity == null) return;

        if (this.getWiredData() == null || this.getWiredData().getSelectedIds() == null || this.getWiredData().getSelectedIds().isEmpty()) {
            event.entity = null;
            return;
        }

        Long itemId = WiredUtil.getRandomElement(this.getWiredData().getSelectedIds());

        if (itemId == null) {
            event.entity = null;
            return;
        }

        RoomItemFloor item = this.getRoom().getItems().getFloorItem(itemId);

        if (item == null || item.isAtDoor() || item.getPosition() == null || item.getTile() == null) {
            event.entity = null;
            return;
        }

        Position position = new Position(item.getPosition().getX(), item.getPosition().getY(), item.getTile().getWalkHeight());

        event.entity.applyEffect(new PlayerEffect(4, 5));

        event.entity.cancelWalk();
        event.entity.warp(position);

        event.entity = null;
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
