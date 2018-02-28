package com.cometproject.server.network.messages.incoming.catalog.pets;

import com.cometproject.server.composers.catalog.pets.ValidatePetNameMessageComposer;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;


public class ValidatePetNameMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String name = msg.readString();
        int errorCode = PetManager.getInstance().validatePetName(name);
        String data = null;

        switch (errorCode) {
            case 1:
                // LONG
                // TODO: put in config
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

        client.send(new ValidatePetNameMessageComposer(errorCode, data));
    }
}
