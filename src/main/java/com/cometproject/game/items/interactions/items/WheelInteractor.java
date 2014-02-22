package com.cometproject.game.items.interactions.items;


import com.cometproject.game.items.interactions.Interactor;
import com.cometproject.game.rooms.entities.types.PlayerEntity;
import com.cometproject.game.rooms.items.RoomItem;
import com.cometproject.game.rooms.types.Room;

import java.util.Random;

public class WheelInteractor extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar) {
        int wheelPos = new Random().nextInt(10) + 1;

        item.setExtraData(Integer.toString(wheelPos));
        item.sendUpdate();

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
    public boolean onTick(RoomItem item) {
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
