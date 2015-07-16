package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class RoomForwardMessageComposer extends MessageComposer {
    private final int roomId;

    public RoomForwardMessageComposer(final int roomId) {
        this.roomId = roomId;
    }

    @Override
    public short getId() {
        return Composers.RoomForwardMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(roomId);
    }
}
