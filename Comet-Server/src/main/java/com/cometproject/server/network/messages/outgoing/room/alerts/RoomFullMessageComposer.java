package com.cometproject.server.network.messages.outgoing.room.alerts;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;


public class RoomFullMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.RoomEnterErrorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeString("/x363");

    }
}
