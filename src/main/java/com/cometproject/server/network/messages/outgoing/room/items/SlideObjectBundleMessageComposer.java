package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class SlideObjectBundleMessageComposer {
    public static Composer compose(Position from, Position to, int rollerItemId, int avatarId, int itemId) {
        Composer msg = new Composer(Composers.ItemAnimationMessageComposer);

        boolean isItem = itemId > 0;

        msg.writeInt(from.getX());
        msg.writeInt(from.getY());
        msg.writeInt(to.getX());
        msg.writeInt(to.getY());
        msg.writeInt(isItem ? 1 : 0);

        if (isItem) {
            msg.writeInt(itemId);
        } else {
            msg.writeInt(rollerItemId);
            msg.writeInt(2);
            msg.writeInt(avatarId);
        }

        msg.writeDouble(from.getZ());
        msg.writeDouble(to.getZ());

        if (isItem) {
            msg.writeInt(rollerItemId);
        }

        return msg;
    }
}
