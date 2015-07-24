package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class YouAreSpectatorMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.YouAreSpectatorMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
