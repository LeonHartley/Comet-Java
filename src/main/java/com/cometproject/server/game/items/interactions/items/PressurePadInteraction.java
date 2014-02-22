package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public class PressurePadInteraction extends Interactor {

    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        /*if(item.getExtraData().equals("0")) {
            item.setExtraData("1");
        } else if(item.getExtraData().equals("1")) {
            item.setExtraData("0");
        }*/

        if(state)
            item.setExtraData("1");
        else
            item.setExtraData("0");

        item.sendUpdate();
        item.saveData();
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar) {
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
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
