package com.cometsrv.network.messages.incoming.room.moderation;

import com.cometsrv.game.rooms.avatars.Avatar;
import com.cometsrv.game.rooms.types.Room;
import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.permissions.AccessLevelMessageComposer;
import com.cometsrv.network.messages.outgoing.room.permissions.RemovePowersMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class GiveRightsMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int avatarId = msg.readInt();

        Room room = client.getPlayer().getAvatar().getRoom();

        if(room == null) return;

        Avatar avatar = room.getAvatars().get(avatarId);

        if(avatar == null) return;
        if(room.getRights().hasRights(avatarId)) return;

        room.getRights().addRights(avatarId);
        avatar.getPlayer().getSession().send(AccessLevelMessageComposer.compose(1));
        client.send(RemovePowersMessageComposer.compose(avatarId, room.getId()));
    }
}
