package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.api.game.rooms.entities.RoomEntityStatus;
import com.cometproject.server.game.commands.user.SitCommand;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.protocol.messages.MessageEvent;

public class SitMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        if (client.getPlayer().getEntity() != null) {
            PlayerEntity playerEntity = client.getPlayer().getEntity();

            if (!client.getPlayer().getEntity().isVisible()) {
                return;
            }

            if (!playerEntity.hasStatus(RoomEntityStatus.SIT)) {
                playerEntity.sit(0.5, playerEntity.getBodyRotation());
            }
        }
    }
}
