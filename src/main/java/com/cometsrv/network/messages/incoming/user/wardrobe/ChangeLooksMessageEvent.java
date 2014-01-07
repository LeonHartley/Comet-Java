package com.cometsrv.network.messages.incoming.user.wardrobe;

import com.cometsrv.network.messages.incoming.IEvent;
import com.cometsrv.network.messages.outgoing.room.avatar.UpdateInfoMessageComposer;
import com.cometsrv.network.messages.types.Event;
import com.cometsrv.network.sessions.Session;

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
