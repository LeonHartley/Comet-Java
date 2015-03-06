package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class FloodFilterMessageComposer extends MessageComposer {
    private final int seconds;

    public FloodFilterMessageComposer(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public short getId() {
        return Composers.FloodFilterMessageComposer;
    }

    @Override
    public void compose(Composer msg) {

    }
}
