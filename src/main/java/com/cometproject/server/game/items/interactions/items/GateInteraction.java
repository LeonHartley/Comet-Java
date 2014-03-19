package com.cometproject.server.game.items.interactions.items;

import com.cometproject.server.game.items.interactions.Interactor;
import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.items.FloorItem;
import com.cometproject.server.game.rooms.items.RoomItem;
import com.cometproject.server.game.rooms.types.Room;

public class GateInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, PlayerEntity avatar, boolean isWiredTriggered) {
        if(item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) {
            item.setExtraData("0");
        }

        if(item.getExtraData().equals("0")) {
            item.setExtraData("1");
            item.saveData();
            item.sendUpdate();

        } else if(item.getExtraData().equals("1")) {
            item.setExtraData("0");
            item.saveData();
            item.sendUpdate();
        }

        // Reload mapping!
        ((FloorItem) item).getRoom().getMapping().updateTile(item.getX(), item.getY());
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
    public boolean onTick(RoomItem item, PlayerEntity avatar, int updateState) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}