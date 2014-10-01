package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.user.profile.UserBadgesMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class UserBadgesMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int userId = msg.readInt();

        if (client.getPlayer().getId() == userId) {
            client.send(UserBadgesMessageComposer.compose(client.getPlayer().getId(), client.getPlayer().getInventory().equippedBadges()));
            return;
        }

        if (client.getPlayer().getEntity() == null)
            return;

        PlayerEntity playerEntity = client.getPlayer().getEntity().getRoom().getEntities().getEntityByPlayerId(userId);

        if (playerEntity != null) {
            client.send(UserBadgesMessageComposer.compose(playerEntity.getPlayerId(), playerEntity.getPlayer().getInventory().equippedBadges()));
        }
    }
}
