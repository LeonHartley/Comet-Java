package com.cometproject.server.game.items.interactions.banzai;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.GenericEntity;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public class BanzaiScoreInteraction extends Interactor {
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
        // TODO: this
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
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
