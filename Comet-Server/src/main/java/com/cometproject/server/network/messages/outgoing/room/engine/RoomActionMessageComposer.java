package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class RoomActionMessageComposer extends MessageComposer {
    private final int action;

    public RoomActionMessageComposer(int action) {
        this.action = action;
    }

    @Override
    public short getId() {
        return Composers.RoomActionMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(action);
    }
}
