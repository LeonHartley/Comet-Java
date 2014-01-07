package com.cometsrv.network.messages.incoming.room.moderation;

import com.cometsrv.game.rooms.entities.types.PlayerEntity;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.RemovePowersMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GiveRightsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int virtualEntityId = msg.readInt();

        Room room = client.getPlayer().getEntity().getRoom();

        if(room == null) return;

        //Avatar avatar = room.getAvatars().get(avatarId);

        PlayerEntity playerEntity = room.getEntities().tryGetPlayerEntityNullable(virtualEntityId);

        if(playerEntity == null) {
            return;
        }

        if(room.getRights().hasRights(playerEntity.getPlayerId())) {
            return;
        }

        room.getRights().addRights(playerEntity.getPlayerId());
        
        playerEntity.getPlayer().getSession().send(AccessLevelMessageComposer.compose(1));
        client.send(RemovePowersMessageComposer.compose(virtualEntityId, room.getId()));
    }
}
