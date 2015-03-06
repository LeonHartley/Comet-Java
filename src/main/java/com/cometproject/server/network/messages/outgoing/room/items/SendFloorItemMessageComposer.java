package com.cometproject.server.network.messages.outgoing.room.items;

import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class SendFloorItemMessageComposer extends MessageComposer {
    private final RoomItemFloor itemFloor;

    public SendFloorItemMessageComposer(RoomItemFloor itemFloor) {
        this.itemFloor = itemFloor;
    }

    @Override
    public short getId() {
        return Composers.AddFloorItemMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        this.itemFloor.serialize(msg, true);
    }
}
