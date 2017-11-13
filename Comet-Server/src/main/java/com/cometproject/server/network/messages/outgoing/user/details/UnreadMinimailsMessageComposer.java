package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.messages.MessageComposer;


public class UnreadMinimailsMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return 0;
    }

    @Override
    public void compose(IComposer msg) {
        // TODO: Minimail
        msg.writeInt(0);
    }
}
