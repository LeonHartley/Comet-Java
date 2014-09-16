package com.cometproject.server.game.rooms.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.types.floor.wired.base.WiredTriggerItem;

public class WiredTriggerPlayerSaysKeyword extends WiredTriggerItem {
    public static final int PARAM_OWNERONLY = 0;

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
    public WiredTriggerPlayerSaysKeyword(int id, int itemId, int roomId, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, roomId, owner, x, y, z, rotation, data);
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 0;
    }

    public static boolean executeTriggers(PlayerEntity playerEntity, String message) {
        boolean wasExecuted = false;


        for(RoomItemFloor floorItem : playerEntity.getRoom().getItems().getByInteraction("wf_trg_says_something")) {
            WiredTriggerPlayerSaysKeyword trigger = ((WiredTriggerPlayerSaysKeyword) floorItem);

            boolean ownerOnly = trigger.getWiredData().getParams().containsKey(PARAM_OWNERONLY) && trigger.getWiredData().getParams().get(PARAM_OWNERONLY) != 0;
            boolean isOwner = playerEntity.getPlayerId() == trigger.getRoom().getData().getOwnerId();

            if(!ownerOnly || isOwner) {
                if (message.contains(trigger.getWiredData().getText())) {
                    wasExecuted = trigger.evaluate(playerEntity, message);
                }
            }
        }

        return wasExecuted;
    }
}
