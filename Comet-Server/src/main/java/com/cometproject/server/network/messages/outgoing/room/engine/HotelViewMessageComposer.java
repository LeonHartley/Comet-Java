package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class HotelViewMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.OutOfRoomMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeShort(2);
    }
}
