package com.cometproject.server.network.messages.outgoing.room.items.wired;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;


public class SaveWiredMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.SaveWiredMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {

    }
}
