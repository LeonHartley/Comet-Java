package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class SendWallItemMessageComposer extends MessageComposer {
    private final RoomItemWall itemWall;

    public SendWallItemMessageComposer(RoomItemWall itemWall) {
        this.itemWall = itemWall;
    }

    @Override
    public short getId() {
        return Composers.ItemAddMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        this.itemWall.serialize(msg);
    }
}
