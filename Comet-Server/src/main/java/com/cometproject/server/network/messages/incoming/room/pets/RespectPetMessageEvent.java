package com.cometproject.server.network.messages.incoming.room.pets;

import com.cometproject.server.game.achievements.types.AchievementType;
import com.cometproject.server.game.rooms.objects.entities.types.PetEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.ActionMessageComposer;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class RespectPetMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        int petId = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) return;

        Room room = client.getPlayer().getEntity().getRoom();
        PetEntity petEntity = room.getEntities().getEntityByPetId(petId);

        if (petEntity == null) return;

        room.getEntities().broadcastMessage(new ActionMessageComposer(client.getPlayer().getEntity().getId(), 7));

        client.getPlayer().getAchievements().progressAchievement(AchievementType.PET_RESPECT_GIVEN, 1);
        // TODO: Flesh this out more
    }
}
