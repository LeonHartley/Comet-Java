package com.cometsrv.network.messages.incoming.room.action;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.DanceMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ApplyDanceMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        int danceId = msg.readInt();

        if(client.getPlayer().getAvatar().getDanceId() == danceId)
            return;

        client.getPlayer().getAvatar().setDanceId(danceId);
        client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(DanceMessageComposer.compose(client.getPlayer().getId(), danceId));
    }
}
