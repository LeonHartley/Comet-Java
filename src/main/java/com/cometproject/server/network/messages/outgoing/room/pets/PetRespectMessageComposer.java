package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

public class PetRespectMessageComposer {
    public static Composer compose(int entityId) {
        Composer msg = new Composer(Composers.RespectPetMessageComposer);

        msg.writeInt(entityId);
        msg.writeBoolean(true); //??

        return msg;
    }
}
