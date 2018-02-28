package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class RoomPropertyMessageComposer extends MessageComposer {
    private final String key;
    private final String value;

    public RoomPropertyMessageComposer(final String key, final String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public short getId() {
        return Composers.RoomPropertyMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.key);
        msg.writeString(this.value);
    }
}
