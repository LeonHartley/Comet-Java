package com.cometproject.server.game.rooms.objects.items.types.floor.wired.triggers;

import com.cometproject.api.game.rooms.objects.data.RoomItemData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.types.floor.wired.base.WiredTriggerItem;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.outgoing.room.avatar.WhisperMessageComposer;


public class WiredTriggerPlayerSaysKeyword extends WiredTriggerItem {
    public static final int PARAM_OWNERONLY = 0;

    public WiredTriggerPlayerSaysKeyword(RoomItemData itemData, Room room) {
        super(itemData, room);
    }

    public static boolean executeTriggers(PlayerEntity playerEntity, String message) {
        boolean wasExecuted = false;

        for (RoomItemFloor floorItem : getTriggers(playerEntity.getRoom(), WiredTriggerPlayerSaysKeyword.class)) {
            WiredTriggerPlayerSaysKeyword trigger = ((WiredTriggerPlayerSaysKeyword) floorItem);

            final boolean ownerOnly = trigger.getWiredData().getParams().containsKey(PARAM_OWNERONLY) && trigger.getWiredData().getParams().get(PARAM_OWNERONLY) != 0;
            final boolean isOwner = playerEntity.getPlayerId() == trigger.getRoom().getData().getOwnerId();

            if (!ownerOnly || isOwner) {
                if (!trigger.getWiredData().getText().isEmpty() && message.toLowerCase().equals(trigger.getWiredData().getText().toLowerCase())) {
                    wasExecuted = trigger.evaluate(playerEntity, message);
                }
            }
        }

        if (wasExecuted) {
            playerEntity.getPlayer().getSession().send(new WhisperMessageComposer(playerEntity.getId(), message));
        }

        return wasExecuted;
    }

    @Override
    public boolean suppliesPlayer() {
        return true;
    }

    @Override
    public int getInterface() {
        return 0;
    }
}
