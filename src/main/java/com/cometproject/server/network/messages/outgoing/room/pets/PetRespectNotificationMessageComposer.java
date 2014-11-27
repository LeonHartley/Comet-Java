package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PetRespectNotificationMessageComposer {
    public static Composer compose(int entityId) {
        Composer msg = new Composer(Composers.PetRespectNotificationMessageComposer);

        msg.writeInt(1);
        msg.writeInt(entityId);

        return msg;
    }
}
