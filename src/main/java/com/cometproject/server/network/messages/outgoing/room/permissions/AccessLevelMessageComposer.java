package com.cometproject.server.network.messages.outgoing.room.permissions;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class AccessLevelMessageComposer {
    public static Composer compose(int rightId) {
        Composer msg = new Composer(Composers.RoomRightsLevelMessageComposer);

        msg.writeInt(rightId);

        return msg;
    }
}
