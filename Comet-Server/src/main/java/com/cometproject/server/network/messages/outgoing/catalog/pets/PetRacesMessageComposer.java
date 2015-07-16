package com.cometproject.server.network.messages.outgoing.catalog.pets;

import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.network.messages.composers.MessageComposer;
import com.cometproject.server.protocol.headers.Composers;

import java.util.List;


public class PetRacesMessageComposer extends MessageComposer {
    private final String raceString;
    private final List<PetRace> races;

    public PetRacesMessageComposer(final String raceString, final List<PetRace> races) {
        this.raceString = raceString;
        this.races = races;
    }

    @Override
    public short getId() {
        return Composers.SellablePetBreedsMessageComposer;
    }

    @Override
    public void compose(IComposer msg) {
        msg.writeString(this.raceString);
        msg.writeInt(this.races.size());

        for (PetRace race : this.races) {
            msg.writeInt(race.getRaceId());
            msg.writeInt(race.getColour1());
            msg.writeInt(race.getColour2());
            msg.writeBoolean(race.hasColour1());
            msg.writeBoolean(race.hasColour2());
        }

    }
}
