package com.cometsrv.network.messages.incoming.room.moderation;

import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class KickUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int virtualEntityId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        PlayerEntity playerEntity = room.getEntities().tryGetPlayerEntityNullable(virtualEntityId);

        if (playerEntity == null) {
            return;
        }

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());


        if(isOwner || hasRights) {
            if(room.getData().getOwnerId() == playerEntity.getPlayerId()) {
                return;
            }

            playerEntity.leaveRoom(false, true, true);
        }
    }
}
