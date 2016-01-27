package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.pets.ScratchPetNotificationMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class ScratchPetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int petId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();
        PetEntity petEntity = room.getEntities().getEntityByPetId(petId);
        PlayerEntity playerEntity = client.getPlayer().getEntity();

        if (petEntity == null) return;

        if(!playerEntity.getPosition().touching(petEntity.getPosition())) {
            playerEntity.moveTo(petEntity.getPosition().squareInFront(petEntity.getBodyRotation()));

            return;
        }

        room.getEntities().broadcastMessage(new ActionMessageComposer(client.getPlayer().getEntity().getId(), 7));

        room.getEntities().broadcastMessage(new ScratchPetNotificationMessageComposer(petEntity));

        petEntity.getData().incrementScratches();
        petEntity.getPetAI().increaseExperience(10);
        petEntity.getData().saveStats();

        client.getPlayer().getAchievements().progressAchievement(AchievementType.PET_RESPECT_GIVEN, 1);
    }
}
