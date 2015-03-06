package com.cometproject.server.network.messages.outgoing.room.access;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class DoorbellAcceptedComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.DoorbellOpenedMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeString("");
    }
}
