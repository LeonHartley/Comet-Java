package com.cometproject.server.network.messages.outgoing.room.pets;

import com.cometproject.server.game.pets.data.PetData;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class PetInformationMessageComposer {
    public static Composer compose(PetData data) {
        Composer msg = new Composer(Composers.PetInfoMessageComposer);

        msg.writeInt(data.getId());
        msg.writeString(data.getName());
        msg.writeInt(data.getLevel());
        msg.writeInt(20); // MAX_LEVEL
        msg.writeInt(data.getExperience());
        msg.writeInt(200); // EXPERIENCE_GOAL
        msg.writeInt(data.getEnergy());
        msg.writeInt(100); // MAX_ENERGY
        msg.writeInt(100); // NUTRITION
        msg.writeInt(100); // MAX_NUTRITION
        msg.writeInt(0); // SCRATCHES
        msg.writeInt(data.getOwnerId());
        msg.writeInt(0); // AGE
        msg.writeString(PlayerDao.getUsernameByPlayerId(data.getOwnerId()));
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

        return msg;
    }

    public static Composer compose(PlayerEntity data) {
        Composer msg = new Composer(Composers.PetInfoMessageComposer);

        msg.writeInt(data.getPlayerId());
        msg.writeString(data.getUsername());
        msg.writeInt(20);
        msg.writeInt(20); // MAX_LEVEL
        msg.writeInt(0);
        msg.writeInt(200); // EXPERIENCE_GOAL
        msg.writeInt(0);
        msg.writeInt(100); // MAX_ENERGY
        msg.writeInt(100); // NUTRITION
        msg.writeInt(100); // MAX_NUTRITION
        msg.writeInt(0); // SCRATCHES
        msg.writeInt(data.getPlayerId());
        msg.writeInt(0); // AGE
        msg.writeString(data.getUsername());
        msg.writeInt(1);
        msg.writeBoolean(data.getMotto().toLowerCase().startsWith("rideable")); // HAS_SADDLE
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

        return msg;
    }
}
