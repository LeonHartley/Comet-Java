package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class UnreadMinimailsMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.MinimailCountMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        // TODO: Minimail
        msg.writeInt(0);
    }
}
