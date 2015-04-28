package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class YouAreControllerMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.YouAreControllerMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
