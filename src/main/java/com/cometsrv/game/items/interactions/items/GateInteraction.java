package com.cometsrv.game.items.interactions.items;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;

public class GateInteraction extends Interactor{
    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        if(item.getExtraData().isEmpty() || item.getExtraData().equals(" ")) {
            item.setExtraData("0");
        }

        if(item.getExtraData().equals("0")) {
            item.setExtraData("1");
            item.saveData();
            item.sendUpdate(avatar.getPlayer().getSession());

        } else if(item.getExtraData().equals("1")) {
            item.setExtraData("0");
            item.saveData();
            item.sendUpdate(avatar.getPlayer().getSession());
        }

        return false;
    }

    @Override
    public boolean onPlace(FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onPickup(FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onTick(FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}