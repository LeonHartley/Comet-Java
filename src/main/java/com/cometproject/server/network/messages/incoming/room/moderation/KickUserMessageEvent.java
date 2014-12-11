package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.game.rooms.types.misc.settings.RoomKickState;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class KickUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int playerId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

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

        playerEntity.leaveRoom(false, true, true);

    }
}
