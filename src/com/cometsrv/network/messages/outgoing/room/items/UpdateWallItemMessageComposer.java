package com.cometsrv.network.messages.outgoing.room.items;

import com.cometsrv.game.rooms.items.WallItem;
import com.cometsrv.network.messages.headers.Composers;
import com.cometsrv.network.messages.types.Composer;

public class UpdateWallItemMessageComposer {
    public static Composer compose(WallItem item, int ownerId, String owner) {
        Composer msg = new Composer(Composers.UpdateWallItemMessageComposer);

        msg.writeString(item.getId());
        msg.writeInt(item.getDefinition().getSpriteId());
        msg.writeString(item.getPosition());
        msg.writeString(item.getExtraData());
        msg.writeInt(!item.getDefinition().getInteraction().equals("default") ? 1 : 0);
        msg.writeInt(ownerId);
        msg.writeString(owner);

        return msg;
    }
}
