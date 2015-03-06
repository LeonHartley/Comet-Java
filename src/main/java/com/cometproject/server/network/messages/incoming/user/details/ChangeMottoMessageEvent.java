package com.cometproject.server.network.messages.incoming.user.details;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;


public class ChangeMottoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String motto = msg.readString();

        client.getPlayer().getData().setMotto(motto);
        client.getPlayer().getData().save();

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new UpdateInfoMessageComposer(client.getPlayer().getEntity()));
        client.send(new UpdateInfoMessageComposer(-1, client.getPlayer().getEntity()));
    }
}
