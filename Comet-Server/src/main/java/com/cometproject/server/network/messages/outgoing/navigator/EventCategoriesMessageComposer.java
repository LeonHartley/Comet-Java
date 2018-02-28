package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class EventCategoriesMessageComposer extends MessageComposer {
    @Override
    public short getId() {
        return Composers.NavigatorFlatCatsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);

        msg.writeInt(1);
        msg.writeString("Promoted Rooms");
        msg.writeBoolean(true);
    }
}
