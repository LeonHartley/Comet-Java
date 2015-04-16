package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class DoorbellRequestComposer extends MessageComposer {
    private final String username;

    public DoorbellRequestComposer(final String username) {
        this.username = username;
    }

    @Override
    public short getId() {
        return Composers.DoorbellMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeString(this.username);
    }
}
