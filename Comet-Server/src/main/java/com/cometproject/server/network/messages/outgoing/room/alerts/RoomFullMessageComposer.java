package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class RoomFullMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.RoomEnterErrorMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(1);
        msg.writeString("/x363");

    }
}
