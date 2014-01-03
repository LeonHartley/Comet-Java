package com.cometsrv.game.items.interactions.wired.action;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class WiredActionMoveRotate extends Interactor {

    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        avatar.getPlayer().getSession().send(AdvancedAlertMessageComposer.compose("Furniture Interaction", "you walked on a wired action."));
        return false;
    }

    @Override
    public boolean onInteract(int state, FloorItem item, Avatar avatar) {
        avatar.getPlayer().getSession().getLogger().debug("Wired not implemented: " + this.getClass().getName());
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
        return true;
    }
}
