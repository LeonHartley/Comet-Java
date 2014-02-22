package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.avatars.misc.Position3D;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class SlideObjectBundleMessageComposer {
    public static Composer compose(Position3D from, Position3D to, int RollerItemId, int AvatarId, int ItemId) {
        Composer msg = new Composer(Composers.SlideObjectBundleMessageComposer);

        boolean isItem = ItemId > 0;

        msg.writeInt(from.getX());
        msg.writeInt(from.getY());
        msg.writeInt(to.getX());
        msg.writeInt(to.getY());
        msg.writeInt(isItem ? 1 : 0);

        if (isItem) {
            msg.writeInt(ItemId);
        } else {
            msg.writeInt(RollerItemId);
            msg.writeInt(2);
            msg.writeInt(AvatarId);
        }

        msg.writeDouble(from.getZ());
        msg.writeDouble(to.getZ());

        if (isItem) {
            msg.writeInt(RollerItemId);
        }

        return msg;
    }
}
