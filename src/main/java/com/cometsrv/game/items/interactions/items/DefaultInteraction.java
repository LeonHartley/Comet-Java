package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;

public class DefaultInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        item.handleInteraction(true);
        item.sendUpdate();
        item.saveData();

        return false;
    }

    @Override
    public boolean onPlace(FloorItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onPickup(FloorItem item, Avatar avatar, Room room) {
        return false;
    }

    @Override
    public boolean onTick(FloorItem item) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
