package com.cometsrv.game.items.interactions.wired.action;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.items.RoomItem;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.game.wired.WiredStaticConfig;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.outgoing.misc.AdvancedAlertMessageComposer;
import com.cometsrv.network.messages.types.Composer;

public class WiredActionMoveRotate extends Interactor {

    @Override
    public boolean onWalk(boolean state, RoomItem item, PlayerEntity avatar) {
        avatar.getPlayer().getSession().send(AdvancedAlertMessageComposer.compose("Furniture Interaction", "you walked on a wired action."));
        return false;
    }

    @Override
    public boolean onPreWalk(RoomItem item, PlayerEntity avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int state, RoomItem item, PlayerEntity avatar) {
        // TODO: This - this is just a test to see if i had the effectId correct (9)
        // TODO: Move wiredeffectmessagecomposer to it's own class!!

        Composer msg = new Composer(Composers.WiredEffectMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(WiredStaticConfig.MAX_FURNI_SELECTION);

        msg.writeInt(0);

        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeInt(item.getId());

        msg.writeString("");
        msg.writeInt(0);
        msg.writeInt(9);
        msg.writeInt(0);
        msg.writeInt(1); // 0.5s
        msg.writeInt(0);
        msg.writeString("");

        avatar.getPlayer().getSession().send(msg);
        //avatar.getPlayer().getSession().getLogger().debug("Wired not implemented: " + this.getClass().getName());
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
        return true;
    }
}
