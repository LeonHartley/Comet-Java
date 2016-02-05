package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;


public class WiredTriggerEnterRoom extends WiredTriggerItem {

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
    public WiredTriggerEnterRoom(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 7;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    public static void executeTriggers(PlayerEntity playerEntity) {
        if(playerEntity == null || playerEntity.getRoom() == null || playerEntity.getRoom().getItems() == null) {
            return;
        }

        for (RoomItemFloor floorItem : playerEntity.getRoom().getItems().getByClass(WiredTriggerEnterRoom.class)) {
            if (!(floorItem instanceof WiredTriggerEnterRoom)) continue;

            WiredTriggerEnterRoom trigger = ((WiredTriggerEnterRoom) floorItem);

            if (trigger.getWiredData().getText().isEmpty() || trigger.getWiredData().getText().equals(playerEntity.getUsername())) {
                trigger.evaluate(playerEntity, null);
            }
        }
    }
}
