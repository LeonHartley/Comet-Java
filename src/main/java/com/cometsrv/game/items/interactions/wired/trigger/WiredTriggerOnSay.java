package com.cometsrv.game.items.interactions.wired.trigger;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class WiredTriggerOnSay extends Interactor {
    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return false;
    }

    @Override
    public boolean onInteract(int request, FloorItem item, Avatar avatar) {
        Composer msg = new Composer(Composers.WiredTriggerMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(0);
        msg.writeInt(0); // item amount
        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeInt(item.getId());
        msg.writeString(item.getExtraData());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        avatar.getPlayer().getSession().send(msg);
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
