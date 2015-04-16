package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.RoomInstance;
import com.cometproject.api.game.rooms.settings.RoomKickState;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.types.MessageEvent;
import com.cometproject.server.network.sessions.Session;


public class KickUserMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int playerId = msg.readInt();

        RoomInstance room = client.getPlayer().getEntity().getRoom();

        if (room == null) {
            return;
        }

        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);

        if (playerEntity == null) {
            return;
        }

        if (!client.getPlayer().getEntity().getRoom().getRights().hasRights(client.getPlayer().getId())
                && !client.getPlayer().getPermissions().hasPermission("room_full_control") && room.getData().getKickState() != RoomKickState.EVERYONE) {
            return;
        }


        if (room.getData().getOwnerId() == playerEntity.getPlayerId() || playerEntity.getPlayer().getPermissions().hasPermission("room_unkickable")) {
            return;
        }

        playerEntity.kick();
    }
}
