package com.cometproject.network.messages.incoming.user.wardrobe;

import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class ChangeLooksMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String gender = msg.readString();
        String figure = msg.readString();

        client.getPlayer().getData().setGender(gender);
        client.getPlayer().getData().setFigure(figure);
        client.getPlayer().getData().save();

        client.getPlayer().getEntity().unIdle();;

        client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(UpdateInfoMessageComposer.compose(client.getPlayer().getEntity()));
        client.send(UpdateInfoMessageComposer.compose(true, client.getPlayer().getEntity()));
    }
}
