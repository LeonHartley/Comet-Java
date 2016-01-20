package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;


public class WiredTriggerPlayerSaysKeyword extends WiredTriggerItem {
    public static final int PARAM_OWNERONLY = 0;

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
    public WiredTriggerPlayerSaysKeyword(long id, int itemId, Room room, int owner, int x, int y, double z, int rotation, String data) {
        super(id, itemId, room, owner, x, y, z, rotation, data);
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

        for (RoomItemFloor floorItem : playerEntity.getRoom().getItems().getByClass(WiredTriggerPlayerSaysKeyword.class)) {
            WiredTriggerPlayerSaysKeyword trigger = ((WiredTriggerPlayerSaysKeyword) floorItem);

            final boolean ownerOnly = trigger.getWiredData().getParams().containsKey(PARAM_OWNERONLY) && trigger.getWiredData().getParams().get(PARAM_OWNERONLY) != 0;
            final boolean isOwner = playerEntity.getPlayerId() == trigger.getRoom().getData().getOwnerId();

            if (!ownerOnly || isOwner) {
                if (!trigger.getWiredData().getText().isEmpty() && message.equals(trigger.getWiredData().getText())) {
                    wasExecuted = trigger.evaluate(playerEntity, message);
                }
            }
        }

        if (wasExecuted) {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), message));
        }

        return wasExecuted;
    }
}
