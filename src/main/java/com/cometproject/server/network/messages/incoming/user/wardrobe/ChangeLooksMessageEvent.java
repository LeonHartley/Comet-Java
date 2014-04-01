package com.cometproject.server.network.messages.incoming.user.wardrobe;

import com.cometproject.server.network.messages.incoming.IEvent;
import com.cometproject.server.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.server.network.messages.types.Event;
import com.cometproject.server.network.sessions.Session;

public class ChangeLooksMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String gender = msg.readString();
        String figure = msg.readString();

        client.getPlayer().getData().setGender(gender);
        client.getPlayer().getData().setFigure(figure);
        client.getPlayer().getData().save();

        client.getPlayer().getEntity().unIdle();

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(UpdateInfoMessageComposer.compose(client.getPlayer().getEntity()));
        client.send(UpdateInfoMessageComposer.compose(true, client.getPlayer().getEntity()));
    }
}
