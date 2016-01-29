package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.pets.PetTrainingPanelMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class GetPetTrainingPanelMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int petId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();
        PetEntity petEntity = room.getEntities().getEntityByPetId(petId);
        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (petEntity == null) return;

        client.send(new PetTrainingPanelMessageComposer(petEntity.getData()));
    }
}
