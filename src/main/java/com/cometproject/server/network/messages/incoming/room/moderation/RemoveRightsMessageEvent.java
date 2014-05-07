package com.cometproject.server.network.messages.incoming.room.moderation;

import com.cometproject.server.game.rooms.entities.types.PlayerEntity;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.permissions.RemovePowersMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class RemoveRightsMessageEvent implements IEvent {
    @Override
    public void handle(Session client, Event msg) throws Exception {
        int playerId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if (room == null || (room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().hasPermission("room_full_control"))) {
            return;
        }

        PlayerEntity playerEntity = room.getEntities().getEntityByPlayerId(playerId);

        if (playerEntity == null) {
            return;
        }

        if (!room.getRights().hasRights(playerEntity.getPlayerId())) {
            return;
        }

        room.getRights().addRights(playerEntity.getPlayerId());
        playerEntity.getPlayer().getSession().send(AccessLevelMessageComposer.compose(0));
        client.send(RemovePowersMessageComposer.compose(room.getId(), playerId));

        playerEntity.removeStatus("flatctrl");
        playerEntity.addStatus("flatctrl", "0");
        playerEntity.markNeedsUpdate();
    }
}
