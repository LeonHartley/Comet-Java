package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.Map;


public class PetInventoryMessageComposer extends MessageComposer {
    private final Map<Integer, PetData> pets;

    public PetInventoryMessageComposer(final Map<Integer, PetData> pets) {
        this.pets = pets;
    }

    @Override
    public short getId() {
        return Composers.PetInventoryMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(1);
        msg.writeInt(1);

        msg.writeInt(pets.size());

        for (PetData data : pets.values()) {
            msg.writeInt(data.getId());
            msg.writeString(data.getName());
            msg.writeInt(data.getTypeId());
            msg.writeInt(data.getRaceId());
            msg.writeString(data.getColour());
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
        }
    }
}
