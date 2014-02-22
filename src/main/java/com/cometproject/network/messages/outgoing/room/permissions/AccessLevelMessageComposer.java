package com.cometproject.network.messages.outgoing.room.permissions;

import com.cometproject.network.messages.headers.Composers;
import com.cometproject.network.messages.types.Composer;

public class AccessLevelMessageComposer {
    public static Composer compose(int rightId) {
        Composer msg = new Composer(Composers.LoadRightsOnRoomMessageComposer);

        msg.writeInt(rightId);

        return msg;
    }
}
