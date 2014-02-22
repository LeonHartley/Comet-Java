package com.cometproject.server.network.messages.outgoing.room.engine;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class RoomPanelMessageComposer {
    public static Composer compose(int id, boolean hasRights) {
        Composer msg = new Composer(Composers.RoomPanelMessageComposer);
        msg.writeBoolean(true);
        msg.writeInt(id);
        msg.writeBoolean(hasRights);
        return msg;
    }
}
