package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class FloodFilterMessageComposer extends MessageComposer {
    private final double seconds;

    public FloodFilterMessageComposer(double seconds) {
        this.seconds = seconds;
    }

    @Override
    public short getId() {
        return Composers.FloodFilterMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(((int) Math.round(this.seconds)));
    }
}
