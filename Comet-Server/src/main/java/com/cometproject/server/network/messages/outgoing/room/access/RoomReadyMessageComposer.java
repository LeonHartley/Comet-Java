package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class RoomReadyMessageComposer extends MessageComposer {

    private final int id;
    private final String model;

    public RoomReadyMessageComposer(int id, String model) {
        this.id = id;
        this.model = model;
    }

    @Override
    public short getId() {
        return Composers.RoomReadyMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.model);
        msg.writeInt(this.id);
    }
}
