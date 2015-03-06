package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.pets.PetInformationMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class PetInformationMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        if (client.getPlayer().getEntity() == null) return;

        int petId = msg.readInt();

        PetEntity petEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPetId(petId);

        if (petEntity == null) {
            // its a player
            PlayerEntity playerEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPlayerId(petId);

            if (playerEntity != null) {
                client.send(new PetInformationMessageComposer(playerEntity));
            }

            return;
        }

        client.send(new PetInformationMessageComposer(petEntity.getData()));
    }
}
