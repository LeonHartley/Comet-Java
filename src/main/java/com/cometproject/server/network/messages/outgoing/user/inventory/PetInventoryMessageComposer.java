package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.Map;

public class PetInventoryMessageComposer {
    public static Composer compose(Map<Integer, PetData> pets) {
        Composer msg = new Composer(Composers.PetInventoryMessageComposer);

        msg.writeInt(1);
        msg.writeInt(1);

        msg.writeInt(pets.size());

        for(PetData data : pets.values()) {
            msg.writeInt(data.getId());
            msg.writeString(data.getName());
            msg.writeInt(data.getRaceId());
            msg.writeInt(data.getRaceId());
            msg.writeString(data.getColour());
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
        }

        return msg;
    }
}
