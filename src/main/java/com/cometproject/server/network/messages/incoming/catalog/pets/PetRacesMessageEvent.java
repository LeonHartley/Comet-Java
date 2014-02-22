package com.cometproject.server.network.messages.incoming.catalog.pets;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PetRacesMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String petRace = msg.readString();
        String raceId = petRace.split("a0 pet")[1];

    }
}
