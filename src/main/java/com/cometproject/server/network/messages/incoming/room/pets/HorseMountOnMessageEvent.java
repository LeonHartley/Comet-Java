package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class HorseMountOnMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int entityId = msg.readInt();

        if(client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getMountedEntity() != null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) return;

        PetEntity horse = room.getEntities().getEntityByPetId(entityId);

        client.getPlayer().getEntity().setMountedEntity(horse);
        horse.setHasMount(true);
    }
}
