package com.cometsrv.game.items.interactions.wired.action;

import com.cometsrv.game.items.interactions.Interactor;
import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.items.FloorItem;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class WiredActionShowMessage extends Interactor {

    @Override
    public boolean onWalk(boolean state, FloorItem item, Avatar avatar) {
        return true;
    }

    @Override
    public boolean onInteract(int state, FloorItem item, Avatar avatar) {
        Composer msg = new Composer(Composers.WiredEffectMessageComposer);

        msg.writeBoolean(false);
        msg.writeInt(0);
        msg.writeInt(0); // item count ????
        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeInt(item.getId());
        msg.writeString(item.getExtraData());
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(7);
        msg.writeInt(0);
        msg.writeInt(0);
        msg.writeInt(0);

        avatar.getPlayer().getSession().send(msg);
        return true;
    }

    @Override
    public boolean requiresRights() {
        return true;
    }
}
