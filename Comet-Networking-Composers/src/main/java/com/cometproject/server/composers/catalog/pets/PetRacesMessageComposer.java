package com.cometproject.server.composers.catalog.pets;

import com.cometproject.api.game.pets.IPetRace;
import com.cometproject.api.networking.messages.IComposer;
import com.cometproject.server.protocol.headers.Composers;
import com.cometproject.server.protocol.messages.MessageComposer;

import java.util.List;

public class PetRacesMessageComposer extends MessageComposer {
    private final String raceString;
    private final List<IPetRace> races;

    public PetRacesMessageComposer(final String raceString, final List<IPetRace> races) {
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

        for (IPetRace race : this.races) {
            msg.writeInt(race.getRaceId());
            msg.writeInt(race.getColour1());
            msg.writeInt(race.getColour2());
            msg.writeBoolean(race.hasColour1());
            msg.writeBoolean(race.hasColour2());
        }
    }

    @Override
    public void dispose() {
        this.races.clear();
    }
}
