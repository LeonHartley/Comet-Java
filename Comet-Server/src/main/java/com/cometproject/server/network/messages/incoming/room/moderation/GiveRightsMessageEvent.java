package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.objects.entities.RoomEntityStatus;
import com.cometproject.server.game.rooms.objects.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.GiveRoomRighsMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.storage.queries.player.PlayerDao;


public class GiveRightsMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        int playerId = msg.readInt();

        if (playerId == -1) return;

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);

        if (room.getRights().hasRights(playerId)) {
            return;
        }

        room.getRights().addRights(playerId);
        client.send(new GiveRoomRighsMessageComposer(room.getId(), playerId, playerEntity != null ? playerEntity.getUsername() : PlayerDao.getUsernameByPlayerId(playerId)));

        if (playerEntity != null) {
            playerEntity.removeStatus(RoomEntityStatus.CONTROLLER);
            playerEntity.addStatus(RoomEntityStatus.CONTROLLER, "1");

            playerEntity.markNeedsUpdate();
            playerEntity.getPlayer().getSession().send(new AccessLevelMessageComposer(1));
        }
    }
}
