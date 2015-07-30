package com.cometproject.server.network.messages.incoming.room.action;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityType;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateIgnoreStatusMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class IgnoreUserMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String username = msg.readString();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        PlayerEntity playerEntity = (PlayerEntity) client.getPlayer().getEntity().getRoom().getEntities().getEntityByName(username, RoomEntityType.PLAYER);

        if (playerEntity != null) {
            if (playerEntity.getPlayer().getPermissions().getRank().modTool() || playerEntity.getPlayer().getPermissions().getRank().roomIgnorable()) {
                return;
            }

            client.getPlayer().ignorePlayer(playerEntity.getPlayerId());
            client.send(new UpdateIgnoreStatusMessageComposer(1, username));
        }

    }
}
