package com.cometsrv.network.messages.incoming.room.moderation;

import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.RemovePowersMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class RemoveAllRightsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) {
            return;
        }

        if(room.getData().getOwnerId() != client.getPlayer().getId()) {
            return;
        }

        for(Integer id : room.getRights().getAll()) {
            /*Avatar user = room.getAvatars().get(id);

            if(user != null) {
                user.getPlayer().getSession().send(AccessLevelMessageComposer.compose(0));
            }*/

            PlayerEntity playerEntity = room.getEntities().tryGetPlayerEntityNullable(id);

            if (playerEntity == null) {
                playerEntity.getPlayer().getSession().send(AccessLevelMessageComposer.compose(0));
            }

            // Remove rights from the player id
            client.send(RemovePowersMessageComposer.compose(id, room.getId()));
            room.getRights().removeRights(playerEntity.getPlayerId());
        }
    }
}
