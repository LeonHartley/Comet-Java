package com.cometproject.server.game.items.interactions.items;


import com.cometproject.server.game.items.interactions.InteractionAction;
import com.cometproject.server.game.items.interactions.InteractionQueueItem;
import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

import java.util.Random;

public class WheelInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, GenericEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, GenericEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, GenericEntity avatar, boolean isWiredTriggered) {
        item.setExtraData("-1");
        item.sendUpdate();

        item.queueInteraction(new InteractionQueueItem(true, item, InteractionAction.ON_TICK, avatar, 0, 10));
        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, PlayerEntity avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item, GenericEntity avatar, int updateState) {
        int wheelPos = new Random().nextInt(10) + 1;

        item.setExtraData(Integer.toString(wheelPos));
        item.sendUpdate();

        return true;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
