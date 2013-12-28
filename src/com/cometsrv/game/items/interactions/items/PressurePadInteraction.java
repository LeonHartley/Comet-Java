package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;

public class PressurePadInteraction extends Interactor {

    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        item.handleInteraction(state);
        item.sendUpdate(avatar.getPlayer().getSession());
        item.saveData();

        return true;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
