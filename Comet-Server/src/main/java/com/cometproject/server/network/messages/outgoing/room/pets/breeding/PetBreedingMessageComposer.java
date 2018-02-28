package com.cometproject.server.network.messages.outgoing.room.pets.breeding;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.pets.races.PetBreedLevel;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.Map;
import java.util.Set;

public class PetBreedingMessageComposer extends MessageComposer {

    private final int itemId;
    private final int babyType;

    // pets have no gender but i thought these namings were better than pet1 & pet2 lols
    private final IPetData mother;

    private final IPetData father;

    public PetBreedingMessageComposer(final int itemId, final int babyType, final IPetData mother, final IPetData father) {
        this.itemId = itemId;
        this.babyType = babyType;
        this.mother = mother;
        this.father = father;
    }

    @Override
    public short getId() {
        return Composers.PetBreedingMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.itemId);//?? result breed

        msg.writeInt(this.mother.getId());
        msg.writeString(this.mother.getName());
        msg.writeInt(this.mother.getLevel());
        msg.writeString(this.mother.getColour());
        msg.writeString(this.mother.getOwnerName());

        msg.writeInt(this.father.getId());
        msg.writeString(this.father.getName());
        msg.writeInt(this.father.getLevel());
        msg.writeString(this.father.getColour());
        msg.writeString(this.father.getOwnerName());

        if (!PetManager.getInstance().getPetBreedPallets().containsKey(this.babyType)) {
            msg.writeInt(0);//levels
        } else {
            final Map<PetBreedLevel, Set<Integer>> availableBreeds = PetManager.getInstance().getPetBreedPallets().get(this.babyType);

            // available levels count
            msg.writeInt(availableBreeds.size());

            msg.writeInt(1); // 1% probability
            msg.writeInt(availableBreeds.get(PetBreedLevel.EPIC).size());

            for (Integer palletId : availableBreeds.get(PetBreedLevel.EPIC)) {
                msg.writeInt(palletId);
            }

            msg.writeInt(2); // 2% probability
            msg.writeInt(availableBreeds.get(PetBreedLevel.RARE).size());

            for (Integer palletId : availableBreeds.get(PetBreedLevel.RARE)) {
                msg.writeInt(palletId);
            }

            msg.writeInt(5); // 5% probability
            msg.writeInt(availableBreeds.get(PetBreedLevel.UNCOMMON).size());

            for (Integer palletId : availableBreeds.get(PetBreedLevel.UNCOMMON)) {
                msg.writeInt(palletId);
            }

            msg.writeInt(92); // 92% probability
            msg.writeInt(availableBreeds.get(PetBreedLevel.COMMON).size());

            for (Integer palletId : availableBreeds.get(PetBreedLevel.COMMON)) {
                msg.writeInt(palletId);
            }
        }

        msg.writeInt(this.babyType);// race type
    }
}
