package com.cometproject.network.messages.incoming.user.details;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class ChangeMottoMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String motto = msg.readString();

        client.getPlayer().getData().setMotto(motto);
        client.getPlayer().getData().save();

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(UpdateInfoMessageComposer.compose(client.getPlayer().getEntity()));
        client.send(UpdateInfoMessageComposer.compose(true, client.getPlayer().getEntity()));
    }
}
