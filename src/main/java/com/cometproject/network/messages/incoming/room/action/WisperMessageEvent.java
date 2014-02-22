package com.cometproject.network.messages.incoming.room.action;

import com.cometproject.boot.Comet;
import com.cometproject.network.messages.incoming.IEvent;
import com.cometproject.network.messages.outgoing.room.avatar.WisperMessageComposer;
import com.cometproject.network.messages.types.Event;
import com.cometproject.network.sessions.Session;

public class WisperMessageEvent implements IEvent {
    public void handle(Session client, Event msg) {
        String text = msg.readString();

        String user = text.split(" ")[0];
        String message = text.replace(user + " ", "");
        Session userTo = Comet.getServer().getNetwork().getSessions().getByPlayerUsername(user);

        if(userTo == null || userTo == client)
            return;

        if(!client.getPlayer().getEntity().onChat(message))
            return;

        client.send(WisperMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), message));
        userTo.send(WisperMessageComposer.compose(client.getPlayer().getEntity().getVirtualId(), message));
    }
}
