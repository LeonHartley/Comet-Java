package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class EventCategoriesMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.EventCategoriesMessageComposer;
    }

    @Override
    public void compose(Composer msg) {
        msg.writeInt(1);

        msg.writeInt(1);
        msg.writeString("Promoted Rooms");
        msg.writeBoolean(true);
    }
}
