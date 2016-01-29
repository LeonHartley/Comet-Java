package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

public class PetTrainingPanelMessageComposer extends MessageComposer {

    private final PetData petData;

    public PetTrainingPanelMessageComposer(final PetData petData) {
        this.petData = petData;
    }

    @Override
    public short getId() {
        return Composers.PetTrainingPanelMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.petData.getId());

        msg.writeInt(8);
        for(int i = 0; i < 8; i++) {
            msg.writeInt(i);
        }

        // for now we will enable 8 commands, we will move it to levelling up soon.
        msg.writeInt(8);

        for(int i = 0; i < 8; i++) {
            msg.writeInt(i);
        }
    }
}
