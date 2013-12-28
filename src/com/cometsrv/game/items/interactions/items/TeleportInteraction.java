package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class TeleportInteraction extends Interactor {
    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        int pairId = GameEngine.getItems().getTeleportPartner(item.getId());

        if(pairId == 0) {
            return false;
        }




        item.handleInteraction(true);
        item.sendUpdate(avatar.getPlayer().getSession());

        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
