package com.cometproject.server.network.messages.outgoing.room.avatar;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class HandItemMessageComposer {
    public static Composer compose(int avatarId, int handItemId) {
        Composer msg = new Composer(Composers.HandItemMessageComposer);

        msg.writeInt(avatarId);
        msg.writeInt(handItemId);

        return msg;
    }
}
