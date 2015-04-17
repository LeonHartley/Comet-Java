package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class HorseMountOnMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int entityId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getMountedEntity() != null) {
            return;
        }

        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if (room == null) return;

        PetEntity horse = room.getEntities().getEntityByPetId(entityId);

        if (horse == null) {
            // its a user.
            if (entityId == client.getPlayer().getId()) return;

            PlayerEntity playerEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPlayerId(entityId);

            if (playerEntity != null) {
                if (!playerEntity.getMotto().toLowerCase().startsWith("rideable")) {
                    return;
                }


                client.getPlayer().getEntity().setMountedEntity(playerEntity);
                client.getPlayer().getEntity().applyEffect(new PlayerEffect(103, 0));
                client.getPlayer().getEntity().warp(playerEntity.getPosition());

                playerEntity.setOverriden(true);
                playerEntity.setHasMount(true);
            }

            return;
        }

        client.getPlayer().getEntity().setMountedEntity(horse);
        horse.setHasMount(true);
    }
}
