package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.GameEngine;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
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

        Room room = avatar.getRoom();

        FloorItem partner = room.getItems().getFloorItem(pairId);

        if(partner == null) {
            // We'll have to find the item in db and get the room id?
            // TODO: find room where partner tele exists
            return false;
        }

        partner.setExtraData("2");
        partner.setNeedsUpdate(true);

        item.setExtraData("2");
        item.setNeedsUpdate(true);

        GameEngine.getLogger().debug("testest");

        return false;
    }

    @Override
    public boolean requiresRights() {
        return false;
    }
}
