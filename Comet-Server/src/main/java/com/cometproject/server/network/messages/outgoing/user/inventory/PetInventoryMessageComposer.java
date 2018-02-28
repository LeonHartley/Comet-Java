package com.cometproject.server.network.messages.outgoing.user.inventory;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;


public class PetInventoryMessageComposer extends MessageComposer {
    private final Map<Integer, IPetData> pets;

    public PetInventoryMessageComposer(final Map<Integer, IPetData> pets) {
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

        for (IPetData data : pets.values()) {
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
