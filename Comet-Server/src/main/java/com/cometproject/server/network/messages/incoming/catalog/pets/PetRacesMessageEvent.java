package com.cometproject.server.network.messages.incoming.catalog.pets;

import com.cometproject.api.game.pets.IPetRace;
import com.cometproject.server.composers.catalog.pets.PetRacesMessageComposer;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;
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
        List<IPetRace> races = PetManager.getInstance().getRacesByRaceId(raceId);

        client.send(new PetRacesMessageComposer(petRace, races));
    }
}
