package com.cometproject.server.network.messages.outgoing.room.pets.breeding;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class PetBreedingCompleteMessageComposer extends MessageComposer {

    private final int babyPetId;
    private final int rarityLevel;

    public PetBreedingCompleteMessageComposer(final int babyPetId, final int rarityLevel) {
        this.babyPetId = babyPetId;
        this.rarityLevel = rarityLevel;
    }

    @Override
    public short getId() {
        return Composers.PetBreedingCompleteMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.babyPetId);
        msg.writeInt(this.rarityLevel);
    }
}
