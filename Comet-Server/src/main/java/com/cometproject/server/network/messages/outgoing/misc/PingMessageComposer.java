package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PingMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.PingMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
