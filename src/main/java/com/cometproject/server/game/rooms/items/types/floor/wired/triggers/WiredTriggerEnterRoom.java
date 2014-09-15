package com.cometproject.server.game.rooms.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredTriggerItem;


public class WiredTriggerEnterRoom extends WiredTriggerItem {

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
    public WiredTriggerEnterRoom(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public int getInterface() {
        return 7;
    }

    public static void executeTriggers(PlayerEntity playerEntity) {
        for(RoomItemFloor floorItem : playerEntity.getRoom().getItems().getByInteraction("wf_trg_enterroom")) {
            WiredTriggerEnterRoom trigger = ((WiredTriggerEnterRoom) floorItem);

            if(trigger.getWiredData().getText().isEmpty() || trigger.getWiredData().getText().equals(playerEntity.getUsername())) {
                trigger.evaluate(playerEntity, null);
            }
        }
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }
}
