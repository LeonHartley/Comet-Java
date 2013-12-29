package com.cometsrv.network.messages.incoming.room.moderation;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class KickUserMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int avatarId = msg.readInt();

        Room room = client.getPlayer().getAvatar().getRoom();

        if(room == null) return;

        boolean isOwner = client.getPlayer().getId() == room.getData().getOwnerId();
        boolean hasRights = room.getRights().hasRights(client.getPlayer().getId());

        if(isOwner || hasRights) {
            if(room.getData().getOwnerId() == avatarId) {
                return;
            }

            Avatar user = room.getAvatars().get(avatarId);

            if(user != null) {
                user.dispose(false, true, true);
            }
        }
    }
}
