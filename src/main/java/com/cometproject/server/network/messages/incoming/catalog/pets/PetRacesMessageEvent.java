package com.cometproject.server.network.messages.incoming.catalog.pets;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.game.pets.races.PetRace;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.pets.PetRacesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

import java.util.List;

public class PetRacesMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String petRace = msg.readString();
        int raceId = Integer.parseInt(petRace.split("a0 pet")[1]);

        List<PetRace> races = GameEngine.getPets().getRacesByRaceId(raceId);

        client.send(PetRacesMessageComposer.compose(petRace, races));
    }
}
