package com.cometsrv.game.items.interactions.wired.action;

import com.cometsrv.game.items.interactions.InteractionState;
import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;

public class WiredActionMoveRotate extends Interactor {

    @Override
    public InteractionState onWalk(boolean state, FloorItem item, Avatar avatar) {
        avatar.getPlayer().getSession().send(AdvancedAlertMessageComposer.compose("Furniture Interaction", "you walked on a wired action."));
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onInteract(int state, FloorItem item, Avatar avatar) {
        avatar.getPlayer().getSession().getLogger().debug("Wired not implemented: " + this.getClass().getName());
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onPlace(FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onPickup(FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public InteractionState onTick(FloorItem item, Avatar avatar) {
        return InteractionState.FINISHED;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}
