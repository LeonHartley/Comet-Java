package com.cometproject.server.network.messages.outgoing.navigator;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class EventCategoriesMessageComposer {
    public static Composer compose() {
        Composer msg = new Composer(Composers.EventCategoriesMessageComposer);

        msg.writeInt(1);

        msg.writeInt(1);
        msg.writeString("Promoted Rooms");
        msg.writeBoolean(true);

        return msg;
    }
}
