package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class EnableNotificationsMessageComposer extends MessageComposer {

    @Override
    public short getId() {
        return Composers.EnableNotificationsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeBoolean(true);
        msg.writeBoolean(false);
    }
}
