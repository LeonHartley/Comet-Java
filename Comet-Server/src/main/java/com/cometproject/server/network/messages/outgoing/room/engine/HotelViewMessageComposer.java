package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class HotelViewMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.CloseConnectionMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
    }
}
