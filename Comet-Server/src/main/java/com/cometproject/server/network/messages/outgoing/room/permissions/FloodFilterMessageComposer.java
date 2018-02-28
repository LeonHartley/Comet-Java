package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class FloodFilterMessageComposer extends MessageComposer {
    private final double seconds;

    public FloodFilterMessageComposer(double seconds) {
        this.seconds = seconds;
    }

    @Override
    public short getId() {
        return Composers.FloodControlMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(((int) Math.round(this.seconds)));
    }
}
