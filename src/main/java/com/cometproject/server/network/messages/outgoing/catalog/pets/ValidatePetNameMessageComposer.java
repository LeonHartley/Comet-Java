package com.cometproject.server.network.messages.outgoing.catalog.pets;

import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;


public class ValidatePetNameMessageComposer {
    public static Composer compose(int errorCode, String data) {
        Composer msg = new Composer(Composers.CheckPetNameMessageComposer);

        msg.writeInt(errorCode);
        msg.writeString(data);

        return msg;
    }
}
