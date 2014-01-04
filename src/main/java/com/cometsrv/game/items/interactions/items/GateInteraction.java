package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;

public class GateInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, RoomItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, RoomItem item, Avatar avatar) {
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

        return false;
    }

    @Override
    public boolean onPlace(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(RoomItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(RoomItem item) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}