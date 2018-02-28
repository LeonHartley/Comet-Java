package com.cometproject.server.network.messages.outgoing.room.pets.horse;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

public class HorseFigureMessageComposer extends MessageComposer {

    private final PetEntity petEntity;

    public HorseFigureMessageComposer(final PetEntity petEntity) {
        this.petEntity = petEntity;
    }

    @Override
    public short getId() {
        return Composers.PetHorseFigureInformationMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeInt(this.petEntity.getId());
        msg.writeInt(this.petEntity.getData().getId());
        msg.writeInt(this.petEntity.getData().getTypeId());
        msg.writeInt(this.petEntity.getData().getRaceId());
        msg.writeString(this.petEntity.getData().getColour());

        if (this.petEntity.getData().isSaddled()) {
            msg.writeInt(4);
            msg.writeInt(3);
            msg.writeInt(3);

            msg.writeInt(this.petEntity.getData().getHair());
            msg.writeInt(this.petEntity.getData().getHairDye());
            msg.writeInt(2);
            msg.writeInt(this.petEntity.getData().getHair());
            msg.writeInt(this.petEntity.getData().getHairDye());
            msg.writeInt(4);
            msg.writeInt(9);
            msg.writeInt(0);
        } else {
            msg.writeInt(1);
            msg.writeInt(2);
            msg.writeInt(2);

            msg.writeInt(this.petEntity.getData().getHair());
            msg.writeInt(this.petEntity.getData().getHairDye());
            msg.writeInt(3);
            msg.writeInt(this.petEntity.getData().getHair());
            msg.writeInt(this.petEntity.getData().getHairDye());
        }

        msg.writeBoolean(this.petEntity.getData().isSaddled());
        msg.writeBoolean(this.petEntity.getMountedEntity() != null);
    }
}
