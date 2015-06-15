package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class PetInformationMessageComposer extends MessageComposer {

    private final PetData petData;
    private final PlayerEntity player;

    public PetInformationMessageComposer(final PetData petData) {
        this.petData = petData;
        this.player = null;
    }

    public PetInformationMessageComposer(final PlayerEntity playerEntity) {
        this.petData = null;
        this.player = playerEntity;
    }

    @Override
    public short getId() {
        return Composers.PetInfoMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        if (this.petData != null) {
            msg.writeInt(petData.getId());
            msg.writeString(petData.getName());
            msg.writeInt(petData.getLevel());
            msg.writeInt(20); // MAX_LEVEL
            msg.writeInt(petData.getExperience());
            msg.writeInt(200); // EXPERIENCE_GOAL
            msg.writeInt(petData.getEnergy());
            msg.writeInt(100); // MAX_ENERGY
            msg.writeInt(100); // NUTRITION
            msg.writeInt(100); // MAX_NUTRITION
            msg.writeInt(0); // SCRATCHES
            msg.writeInt(petData.getOwnerId());
            msg.writeInt(0); // AGE
            msg.writeString(PlayerDao.getUsernameByPlayerId(petData.getOwnerId()));
            msg.writeInt(1);
            msg.writeBoolean(false); // HAS_SADDLE
            msg.writeBoolean(false); // HAS_RIDER
            msg.writeInt(0);

            // CAN ANYONE MOUNT?
            msg.writeInt(1); // yes = 1 no = 0

            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeBoolean(true); // all can mount
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeBoolean(false);
        } else {
            msg.writeInt(this.player.getPlayerId());
            msg.writeString(this.player.getUsername());
            msg.writeInt(20);
            msg.writeInt(20); // MAX_LEVEL
            msg.writeInt(0);
            msg.writeInt(200); // EXPERIENCE_GOAL
            msg.writeInt(0);
            msg.writeInt(100); // MAX_ENERGY
            msg.writeInt(100); // NUTRITION
            msg.writeInt(100); // MAX_NUTRITION
            msg.writeInt(0); // SCRATCHES
            msg.writeInt(this.player.getPlayerId());
            msg.writeInt(0); // AGE
            msg.writeString(this.player.getUsername());
            msg.writeInt(1);
            msg.writeBoolean(this.player.getMotto().toLowerCase().startsWith("rideable")); // HAS_SADDLE
            msg.writeBoolean(false); // HAS_RIDER
            msg.writeInt(0);

            // CAN ANYONE MOUNT?
            msg.writeInt(1); // yes = 1 no = 0

            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeBoolean(true); // all can mount
            msg.writeInt(0);
            msg.writeString("");
            msg.writeBoolean(false);
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeInt(-1);
            msg.writeBoolean(false);

        }
    }
}
