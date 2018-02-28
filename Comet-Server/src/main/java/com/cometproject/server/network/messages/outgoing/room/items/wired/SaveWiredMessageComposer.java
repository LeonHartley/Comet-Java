package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;


public class SaveWiredMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.HideWiredConfigMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
