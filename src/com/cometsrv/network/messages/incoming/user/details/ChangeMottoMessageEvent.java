package com.cometsrv.network.messages.incoming.user.details;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

public class ChangeMottoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String motto = msg.readString();

        client.getPlayer().getData().setMotto(motto);
        client.getPlayer().getData().save();

        client.getPlayer().getAvatar().getRoom().getAvatars().broadcast(UpdateInfoMessageComposer.compose(client.getPlayer().getData()));
        client.send(UpdateInfoMessageComposer.compose(true, client.getPlayer().getData()));
    }
}
