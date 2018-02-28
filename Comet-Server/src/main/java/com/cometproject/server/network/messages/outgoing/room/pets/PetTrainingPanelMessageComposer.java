package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.api.game.pets.IPetData;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class PetTrainingPanelMessageComposer extends MessageComposer {

    private final IPetData petData;

    public PetTrainingPanelMessageComposer(final IPetData petData) {
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

        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(3);
        msg.writeInt(5);
        msg.writeInt(6);
        msg.writeInt(7);
        msg.writeInt(11);
        msg.writeInt(23);

        // for now we will enable 8 commands, we will move it to levelling up soon.
        msg.writeInt(8);

        // enabled commands
        msg.writeInt(0);
        msg.writeInt(1);
        msg.writeInt(3);
        msg.writeInt(5);
        msg.writeInt(6);
        msg.writeInt(7);
        msg.writeInt(11);
        msg.writeInt(23);
    }
}
