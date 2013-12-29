package com.cometsrv.network.messages.incoming.catalog.pets;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class PetRacesMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) {
        String petRace = msg.readString();
        String raceId = petRace.split("a0 pet")[1];

    }
}
