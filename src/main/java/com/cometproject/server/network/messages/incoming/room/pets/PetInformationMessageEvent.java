package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.entities.types.PetEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.pets.PetInformationMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class PetInformationMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int petId = msg.readInt();

        PetEntity petEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPetId(petId);

        if (petEntity == null) {
            return;
        }

        client.send(PetInformationMessageComposer.compose(petEntity.getData()));
    }
}
