package com.cometproject.server.network.messages.incoming.catalog.pets;

import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.catalog.pets.PetRacesMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import org.apache.commons.lang.StringUtils;

import java.util.List;


public class PetRacesMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) {
        final String petRace = msg.readString();
        final String[] splitRace = petRace.split("a0 pet");

        if (splitRace.length < 2 || !StringUtils.isNumeric(splitRace[1])) {
            return;
        }

        int raceId = Integer.parseInt(splitRace[1]);
        List<PetRace> races = PetManager.getInstance().getRacesByRaceId(raceId);

        client.send(new PetRacesMessageComposer(petRace, races));
    }
}
