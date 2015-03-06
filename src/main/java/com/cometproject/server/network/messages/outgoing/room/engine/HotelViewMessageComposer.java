package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class HotelViewMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.OutOfRoomMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeShort(2);
    }
}
