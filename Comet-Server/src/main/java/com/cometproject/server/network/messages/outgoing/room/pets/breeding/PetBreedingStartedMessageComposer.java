package com.cometproject.server.network.messages.outgoing.room.pets.breeding;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class PetBreedingStartedMessageComposer extends MessageComposer {
    private final int itemId;
    private final int flag;

    public PetBreedingStartedMessageComposer(int itemId, int flag) {
        this.itemId = itemId;
        this.flag = flag;
    }

    @Override
    public short getId() {
        return Composers.PetBreedingStartedMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.itemId);
        msg.writeInt(this.flag);
    }
}
