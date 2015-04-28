package com.cometproject.server.network.messages.outgoing.user.details;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


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
