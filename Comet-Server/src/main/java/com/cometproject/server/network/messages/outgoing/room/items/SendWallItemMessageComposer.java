package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class SendWallItemMessageComposer extends MessageComposer  {
    private final RoomItemWall itemWall;

    public SendWallItemMessageComposer(RoomItemWall itemWall) {
        this.itemWall = itemWall;
    }

    @Override
    public short getId() {
        return Composers.AddWallItemMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        this.itemWall.serialize(msg);
    }
}
