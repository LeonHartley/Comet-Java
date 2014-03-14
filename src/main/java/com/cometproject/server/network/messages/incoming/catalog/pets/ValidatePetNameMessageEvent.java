package com.cometproject.server.network.messages.incoming.catalog.pets;

import com.cometproject.server.game.GameEngine;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.catalog.pets.ValidatePetNameMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ValidatePetNameMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        String name = msg.readString();
        int errorCode = GameEngine.getPets().validatePetName(name);
        String data = null;

        switch(errorCode) {
            case 1:
                // LONG
                // TODO: put in locale
                data = "We expect a maximum of 16 characters!"; // we send the max length we expect
                break;

            case 2:
                // SHORT
                data = "16"; // we send the max length we expect
                break;
            case 3:
                // INVALID CHARACTERS
                break;

            case 4:
                //WORD FILTER
                break;
        }

        client.send(ValidatePetNameMessageComposer.compose(errorCode, data));
    }
}
