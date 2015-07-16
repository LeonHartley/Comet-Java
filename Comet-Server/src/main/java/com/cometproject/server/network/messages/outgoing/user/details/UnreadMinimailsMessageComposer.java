package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class UnreadMinimailsMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.MinimailCountMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        // TODO: Minimail
        msg.writeInt(0);
    }
}
