package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class SlideObjectBundleMessageComposer extends MessageComposer {
    private final Position from;
    private final Position to;
    private final int rollerItemId;
    private final int avatarId;
    private final int itemId;

    public SlideObjectBundleMessageComposer(Position from, Position to, int rollerItemId, int avatarId, int itemId) {
        this.from = from;
        this.to = to;
        this.rollerItemId = rollerItemId;
        this.avatarId = avatarId;
        this.itemId = itemId;
    }

    @Override
    public short getId() {
        return Composers.SlideObjectBundleMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        final boolean isItem = itemId > 0;

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
    }
}
