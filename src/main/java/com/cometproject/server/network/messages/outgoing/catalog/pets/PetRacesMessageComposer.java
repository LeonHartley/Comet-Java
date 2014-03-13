package com.cometproject.server.network.messages.outgoing.catalog.pets;

import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.network.messages.headers.Composers;
import com.cometproject.server.network.messages.types.Composer;

import java.util.List;

public class PetRacesMessageComposer {
    public static Composer compose(String raceString, List<PetRace> races) {
        Composer msg = new Composer(Composers.PetRacesMessageComposer);

        msg.writeString(raceString);
        msg.writeInt(races.size());

        for(PetRace race : races) {
            msg.writeInt(race.getRaceId());
            msg.writeInt(race.getColour1());
            msg.writeInt(race.getColour2());
            msg.writeBoolean(race.hasColour1());
            msg.writeBoolean(race.hasColour2());
        }

        return msg;
    }
}
