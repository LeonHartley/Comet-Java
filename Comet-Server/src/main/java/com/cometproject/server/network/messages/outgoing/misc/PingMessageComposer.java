package com.cometproject.server.network.messages.outgoing.misc;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class PingMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.LatencyResponseMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(0);
    }
}
