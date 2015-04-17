package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class CanCreateRoomMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.CanCreateRoomMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(0);
        msg.writeInt(25);
    }
}
