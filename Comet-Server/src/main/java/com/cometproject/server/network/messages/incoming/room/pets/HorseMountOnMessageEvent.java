package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.rooms.objects.entities.effects.PlayerEffect;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.objects.misc.Position;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.utilities.DistanceCalculator;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.items.SlideObjectBundleMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class HorseMountOnMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int entityId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getMountedEntity() != null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

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

        if(!DistanceCalculator.tilesTouching(client.getPlayer().getEntity().getPosition(), horse.getPosition())) {
            Position closePosition = horse.getPosition().squareBehind(6);

            client.getPlayer().getEntity().moveTo(closePosition.getX(), closePosition.getY());
            return;
        }

        Position pos = horse.getPositionToSet() != null ? horse.getPositionToSet() : horse.getPosition();

        if(horse.isWalking()) {
            horse.setWalkCancelled(true);
        }

        client.getPlayer().getEntity().applyEffect(new PlayerEffect(103, 0));


        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new SlideObjectBundleMessageComposer(client.getPlayer().getEntity().getPosition(), new Position(horse.getPosition().getX(), pos.getY(), pos.getZ() + 1.0), 0, client.getPlayer().getEntity().getId(), 0));

        client.getPlayer().getEntity().setBodyRotation(horse.getBodyRotation());
        client.getPlayer().getEntity().setHeadRotation(horse.getHeadRotation());

        client.getPlayer().getEntity().moveTo(pos.getX(), pos.getY());

        client.getPlayer().getEntity().setMountedEntity(horse);

        horse.setMountedEntity(client.getPlayer().getEntity());
        horse.setHasMount(true);
    }
}
